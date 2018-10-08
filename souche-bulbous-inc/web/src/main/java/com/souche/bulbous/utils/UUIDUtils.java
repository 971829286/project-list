package com.souche.bulbous.utils;

import java.util.Arrays;
import java.util.Base64;
import java.util.UUID;

public class UUIDUtils {
	/**
	 * 生成10位UUID
	 * 
	 * @return
	 */
	public static String getID10() {
		UUID uuid = UUID.randomUUID();

		// 改变uuid的生成规则
		return HashUtils.convertToHashStr(uuid.getMostSignificantBits(), 5)
				+ HashUtils.convertToHashStr(uuid.getLeastSignificantBits(), 5);
	}
	/**
	 * 生成36位UUID
	 */
	public static String getID36(){
		return UUID.randomUUID().toString();
	}
	/**
	 * 生成32位UUID
	 * @return
	 */
	public static String getID32(){
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	/**
	 * 转换目前32位UUID为10位UUID
	 * 
	 * @param uuidStr
	 * @return
	 */
	public static String convertID(String uuidStr) {
		UUID uuid = UUID.fromString(uuidStr);
		// 改变uuid的生成规则
		return HashUtils.convertToHashStr(uuid.getMostSignificantBits(), 5)
				+ HashUtils.convertToHashStr(uuid.getLeastSignificantBits(), 5);
	}

	/**
	 * 将 JDK 自带的 UUID 转为 Base64 的字符串，去除 Base64 中的 "+"、"/"、"="
	 * 最大长度：22，数据库中字段可设置为 char(22)
	 *
	 * @return
	 */
	public static String getShortUUID() {
		UUID uuid = UUID.randomUUID();
		final long mostSignificantBits = uuid.getMostSignificantBits();
		final long leastSignificantBits = uuid.getLeastSignificantBits();

		byte[] mostSignBytes = longToBytes(mostSignificantBits);
		byte[] leastSignBytes = longToBytes(leastSignificantBits);

		byte[] signBytes = concat(mostSignBytes, leastSignBytes);
		String uuidStr = Base64.getEncoder().encodeToString(signBytes)
				.replace("+", "")
				.replace("/", "")
				.replace("=", "");
		return uuidStr;
	}

	private static byte[] longToBytes(final long number) {
		long temp = number;

		final int size = 8;
		byte[] bytes = new byte[size];
		for (int i = 0; i < size; i++) {
			bytes[i] = new Long(temp & 0xff).byteValue(); // 将最低位保存在最低位
			temp = temp >> 8; // 向右移8位
		}
		return bytes;
	}

	/**
	 * 合并数组
	 *
	 * @param first
	 * @param second
	 * @return
	 */
	private static byte[] concat(byte[] first, byte[] second) {
		byte[] result = Arrays.copyOf(first, first.length + second.length);
		System.arraycopy(second, 0, result, first.length, second.length);
		return result;
	}

	public static void main(String[] args) {
		System.out.println(getID32());  
	}

}
