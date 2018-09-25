package cn.ourwill.huiyizhan.utils.page;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 兼容mybatis-helper的分页
 * @param <T>
 */
@Data
public class PageInfo<T> implements Serializable {
//    private static final long serialVersionUID = 1L;
    private int     pageNum;//当前页
    private int     pageSize;//一页多少条记录
    private String  orderBy;//是否排序
    private List<T> list;//
    private Long    total;//总共多少条
    private int     navigatePages;//-->总共显示8页的页码
    private boolean isFirstPage;//是否第一页
    private int     pages;//总共多少页
    private boolean isLastPage;//是否最后一页
    private int     size;//当前页有几条记录
    private int     prePage;//前一页
    private int     nextPage;//后一页
    private boolean hasPreviousPage;//是否有前页
    private boolean hasNextPage;//是否有后页
    private int     startRow;//起始偏移量
    private Long    endRow;//结束偏移量
    private int[]   navigatePageNums;//当前页页码导航栏
    private int     navigateFirstPage;//页码导航栏中的第一页
    private int     navigateLastPage;//页码导航栏中的最后一页
    private int     firstPage;//=navigateFirstPage
    private int     lastPage;//=navigateLastPage

    public PageInfo(List<T> list, int pageNum, int pageSize, long total) {
        this.navigatePages = 8;
        this.list = list;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.total = total;
        this.orderBy = null;
        setIsFirstPage();
        setPages();
        setIsLastPage();
        setSize();
        setPrePage();
        setNextPage();
        setHasPreviousPage();
        setHasNextPage();
        setStartRow();
        setEndRow();
        setNavigatePageNums();
        setNavigateFirstPage();
        setNavigateLastPage();
        this.firstPage = this.navigateFirstPage;
        this.lastPage = this.navigateLastPage;
    }

    private void setIsFirstPage() {
        if (this.pageNum <= 0) {
            this.pageNum = 0;
        }
        this.isFirstPage = (this.pageNum == 1);
    }

    private void setPages() {
        Double temp = Math.ceil((double) this.total / this.pageSize);
        this.pages = temp.intValue();
    }

    private void setIsLastPage() {
        if (this.pageNum >= this.pages) {
            pageNum = pages;
        }
        this.isLastPage = (this.pageNum == pages);
    }

    private void setSize() {
        if (this.pageNum < this.pages && this.pages > 0) {
            this.size = pageSize;
        } else if (pageNum == this.pages) {
            int lastPageSize = (int) (this.total % this.pageSize);
            if (lastPageSize == 0) {
                this.size = pageSize;
            } else {
                this.size = lastPageSize;
            }
        }
    }

    private void setPrePage() {
        if (this.pageNum < 1) {
            this.pageNum = 1;
            this.prePage = 0;
        } else {
            this.prePage = pageNum - 1;
            if (this.prePage < 0) {
                this.prePage = 0;
            }
        }
    }

    private void setNextPage() {
        if (this.pageNum > this.pages) {
            this.pageNum = this.pages;
            this.nextPage = 0;
        } else {
            this.nextPage = this.pageNum + 1;
            if (this.nextPage > this.pages) {
                this.nextPage = 0;
            }
        }
    }

    private void setHasPreviousPage() {
        this.hasPreviousPage = (this.prePage != 0);
    }

    private void setHasNextPage() {
        this.hasNextPage = (this.nextPage != 0);
    }

    private void setStartRow() {
        if (this.pageNum == 1) {
            this.startRow = 1;
        } else {
            this.startRow = (this.pageNum - 1) * this.pageSize + 1;
        }
    }

    private void setEndRow() {
        if (this.pageNum < this.pages) {

            this.endRow = (long) (this.pageSize * this.pageNum);
        } else {
            this.endRow = this.total;
        }
    }

    private void setNavigatePageNums() {
        if (this.pages <= this.navigatePages) {
            int i;
            this.navigatePageNums = new int[this.pages];
            for (i = 0; i < this.pages; ++i) {
                this.navigatePageNums[i] = i + 1;
            }
        } else {
            //当页面总数大于navigatePages时,按照策略输出
            this.navigatePageNums = new int[this.navigatePages];
            //当前页小于=5 正常输出
            if (pageNum <= this.navigatePages / 2 + 1) {
                for (int i = 1; i <= 8; i++) {
                    this.navigatePageNums[i - 1] = i;
                }
                //当前页 >5
            } else {
                //
                int gap = this.pages - this.pageNum;
                //
                //如果当前页到尾页的距离大于4 则正常输出
                if (gap >= 4) {
                    int start = this.pageNum - 4;
                    for (int i = 0; i < this.navigatePages; i++) {
                        this.navigatePageNums[i] = start++;
                    }
                } else {
                    //如果当前带尾页的距离小于4 则以尾页基准
                    int lastPage = this.pages;
                    for (int i = this.navigatePages - 1; i >= 0; i--) {
                        this.navigatePageNums[i] = lastPage--;
                    }
                }
            }
        }
    }

    private void setNavigateFirstPage() {
        this.navigateFirstPage = this.navigatePageNums[0];
    }

    private void setNavigateLastPage() {
        //this.navigateLastPage = this.navigatePageNums[7];
        //this.navigateLastPage = 1;
        if(this.pages < 8){
            this.navigateLastPage = pages;
        }else{
            this.navigateLastPage = this.navigatePageNums[7];
        }
    }
    public String toString() {
        StringBuffer sb = new StringBuffer("PageInfo{");
        sb.append("pageNum=").append(this.pageNum);
        sb.append(", pageSize=").append(this.pageSize);
        sb.append(", size=").append(this.size);
        sb.append(", startRow=").append(this.startRow);
        sb.append(", endRow=").append(this.endRow);
        sb.append(", total=").append(this.total);
        sb.append(", pages=").append(this.pages);
        sb.append(", list=").append(this.list);
        sb.append(", prePage=").append(this.prePage);
        sb.append(", nextPage=").append(this.nextPage);
        sb.append(", isFirstPage=").append(this.isFirstPage);
        sb.append(", isLastPage=").append(this.isLastPage);
        sb.append(", hasPreviousPage=").append(this.hasPreviousPage);
        sb.append(", hasNextPage=").append(this.hasNextPage);
        sb.append(", navigatePages=").append(this.navigatePages);
        sb.append(", navigateFirstPage").append(this.navigateFirstPage);
        sb.append(", navigateLastPage").append(this.navigateLastPage);
        sb.append(", navigatepageNums=");
        if (this.navigatePageNums == null) {
            sb.append("null");
        } else {
            sb.append('[');

            for(int i = 0; i < this.navigatePageNums.length; ++i) {
                sb.append(i == 0 ? "" : ", ").append(this.navigatePageNums[i]);
            }

            sb.append(']');
        }

        sb.append('}');
        return sb.toString();
    }
    //测试方法
    public  void test() {
        int pageSize = 10;
        int pageNum = 11;
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add(i);
        }
        PageInfo pageInfo = new PageInfo(list, pageNum, pageSize, 100);
        System.out.println("pageNum:" + pageInfo.pageNum);
        System.out.println("isFisstPages:" + pageInfo.isFirstPage);
        System.out.println("navigatePages:" + pageInfo.navigatePages);
        System.out.println("total:" + pageInfo.total);
        System.out.println("pages:" + pageInfo.pages);
        System.out.println("isLastPage:" + pageInfo.isLastPage);
        System.out.println("size:" + pageInfo.size);
        System.out.println("prePage:" + pageInfo.prePage);
        System.out.println("nextPage:" + pageInfo.nextPage);
        System.out.println("hasPreviousPage:" + pageInfo.hasPreviousPage);
        System.out.println("hasNextPage:" + pageInfo.hasNextPage);
        System.out.println("startRow:" + pageInfo.startRow);
        System.out.println("endRow:" + pageInfo.endRow);
        System.out.println("navigatePageNums:" + Arrays.toString(pageInfo.navigatePageNums));
        System.out.println("navigateFirstPage:" + pageInfo.navigateFirstPage);
        System.out.println("navigateLastPage:" + pageInfo.navigateLastPage);
    }
}
