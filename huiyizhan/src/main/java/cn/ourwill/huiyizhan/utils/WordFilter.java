package cn.ourwill.huiyizhan.utils;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 敏感词汇过滤
 */
@Component
public class WordFilter {

	// private static WordNode[] nodes = new
	private static final FilterSet set = new FilterSet();
	private static final Map<Integer, WordNode> nodes = new HashMap<Integer, WordNode>(1024, 1);
	private static String wordPath;
//	static {
//		try {
//			long a = System.nanoTime();
//			init();
//			a = System.nanoTime() - a;
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new RuntimeException("初始化过滤器失败");
//		}
//	}

	public static void setWordPath(String wordPaths){
		wordPath=wordPaths;
	}

	// 初始化敏感词库
	public static void init() {

		List<String> words = new ArrayList<>();
		if(GlobalUtils.getFilterWords().size()>0)
			words = GlobalUtils.getFilterWords();
		else{
			BufferedReader br = null;
			try {
				br = new BufferedReader(new InputStreamReader(new FileInputStream(wordPath),"GBK"));
				words = new ArrayList<String>();
				for (String buf = ""; (buf = br.readLine()) != null;) {
					if (buf.isEmpty() || buf == null || buf == "")
						continue;
					words.add(buf);
				}
			} catch (Exception e) {
				e.printStackTrace();
//				throw new RuntimeException(e);
			} finally {
				try {
					if (br != null)
						br.close();
				} catch (IOException e) {
				}
			}
			GlobalUtils.setFilterWords(words);
		}

		// 获取敏感词
		addSensitiveWord(words);
	}

	// 添加敏感词汇
	private static void addSensitiveWord(final List<String> words) {
		char[] chs;
		int fchar;
		int lastIndex;
		WordNode fnode;
		for (String curr : words) {
			chs = curr.toCharArray();
			fchar = chs[0];
			if (!set.contains(fchar)) {
				set.add(fchar);
				fnode = new WordNode(fchar, chs.length == 1);
				nodes.put(fchar, fnode);
			} else {
				fnode = nodes.get(fchar);
				if (!fnode.isLast() && chs.length == 1)
					fnode.setLast(true);
			}
			lastIndex = chs.length - 1;
			for (int i = 1; i < chs.length; i++) {
				fnode = fnode.addIfNoExist(chs[i], i == lastIndex);
			}
		}
	}

	// 实现过滤
	private static final char SIGN = '*';

	public static final String doFilter(final String src) {
//		try {
//			long a = System.nanoTime();
//			init();
//			a = System.nanoTime() - a;
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new RuntimeException("初始化过滤器失败");
//		}
		char[] chs = src.toCharArray();
		int length = chs.length;
		int currc;
		int k;
		WordNode node;
		for (int i = 0; i < length; i++) {
			currc = chs[i];
			if (!set.contains(currc)) {
				continue;
			}
			node = nodes.get(currc);
			if (node == null)
				continue;
			boolean couldMark = false;
			int markNum = -1;
			if (node.isLast()) {
				couldMark = true;
				markNum = 0;
			}
			k = i;
			for (; ++k < length;) {

				node = node.querySub(chs[k]);
				if (node == null)
					break;
				if (node.isLast()) {
					couldMark = true;
					markNum = k - i;
				}
			}
			if (couldMark) {
				for (k = 0; k <= markNum; k++) {
					chs[k + i] = SIGN;
				}
				i = i + markNum;
			}
		}
		return new String(chs);
	}

}
