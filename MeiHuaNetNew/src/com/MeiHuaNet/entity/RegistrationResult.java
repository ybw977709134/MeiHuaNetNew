package com.MeiHuaNet.entity;

/**
 *
 * @description 报名后返回的数据 
 * @author lee
 * @time  2013-12-6 下午2:37:11
 *
 */
public class RegistrationResult {

	/**
	 * 报名结果的信息
	 */
	public String Message;
	/**
	 * 报名成功后返回的编号
	 */
	public String RegCode;
	
	/**
	 * 付费活动报名成功后的订单信息
	 */
	public OrderInfo Order;
	
	public static class OrderInfo{
		/**
		 * 订单编号
		 */
		public String OrderID;
		/**
		 * 用户姓名
		 */
		public String FullName;
		/**
		 * 用户邮件

		 */
		public String Email;
		/**
		 * 订单金额
		 */
		public String Price;
		/**
		 * 活动名称（支付宝中的商品名称）
		 */
		public String Subject;
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "OrderInfo [OrderID=" + OrderID + ", FullName=" + FullName
					+ ", Email=" + Email + ", Price=" + Price + ", Subject="
					+ Subject + "]";
		}
		
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RegistrationResult [Message=" + Message + ", RegCode="
				+ RegCode + ", Order=" + Order + "]";
	}
	
}
