package com.chinasofti.ordersys.mapper;

import java.sql.Date;
import java.util.List;

import com.chinasofti.ordersys.vo.OrderSumInfo;
import com.chinasofti.ordersys.vo.UserInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.mybatis.spring.annotation.Mapper;

import com.chinasofti.ordersys.vo.Cart;
import com.chinasofti.ordersys.vo.OrderInfo;

@Mapper
public interface OrderMapper {

//	@Insert("insert into orderinfo(orderBeginDate,waiterId,tableId) values(#{info.orderBeginDate},#{info.waiterId},#{info.tableId})")
	@Insert("insert into orderinfo(orderBeginDate,waiterId,tableId) values(#{info.orderBeginDate},#{info.waiterId},#{info.tableId})")
	@Options(useGeneratedKeys = true, keyProperty = "info.orderId")
	public void addOrder(@Param("info") OrderInfo info);

	@Insert("insert into orderdishes(orderReference,dishes,num) values(#{key},#{unit.dishesId},#{unit.num})")
	public void addOrderDishesMap(@Param("unit") Cart.CartUnit unit,@Param("key") int key);

	@Select("select * from orderinfo,userInfo where orderState=#{state} and userInfo.userId=orderinfo.waiterId limit #{first},#{max}")
	public List<OrderInfo> getNeedPayOrdersByPage(@Param("first") int first,
			@Param("max") int max, @Param("state") int state);

	@Select("select count(*) from orderinfo where orderState=#{state}")
	public Long getMaxPage(@Param("state") int state);

	@Select("select * from orderinfo,userInfo where orderState=#{state} and userInfo.userId=orderinfo.waiterId")
	public List<OrderInfo> getNeedPayOrders(@Param("state") int state);

	@Update("update orderinfo set orderState=1,orderEndDate=now() where orderId=#{orderId}")
	public void requestPay(@Param("orderId") Integer orderId,
						   @Param("now") Date now);

	@Select("select * from orderinfo,userinfo where orderId=#{orderId} and orderinfo.waiterId=userinfo.userId")
	public OrderInfo getOrderById(@Param("orderId") Integer orderId);

	@Select("SELECT SUM(d.dishesPrice*od.num) FROM orderinfo a,dishesinfo d,orderdishes od WHERE a.orderId=od.orderReference AND od.dishes=d.dishesId AND a.orderId=#{orderId}")
	public Double getSumPriceByOrderId(@Param("orderId") Integer orderId);

	@Select("SELECT * FROM orderinfo o,userinfo u,orderdishes od,dishesinfo d WHERE orderId=#{orderId} AND o.waiterId=u.userId AND od.orderReference=o.orderId AND d.dishesId=od.dishes")
	public List<OrderInfo> getOrderDetailById(@Param("orderId") Integer orderId);

	@Update("update orderinfo set orderState=#{state} where orderId=#{orderId}")
	public void changeState(@Param("orderId") Integer orderId,
			@Param("state") int state);

	@Select("select * from orderinfo,userInfo where orderState=2 and userInfo.userId=orderinfo.waiterId and orderinfo.orderEndDate between #{bd} and #{ed} limit #{curPage},#{pageSize}")
	public List<OrderInfo> getOrderInfoBetweenDate(@Param("bd") Date beginDate,
			@Param("ed") Date endDate,@Param("curPage") int curPage,@Param("pageSize") int pageSize);

	//@Select("select distinct * from orderinfo,orderdishes,dishesinfo where completedState = 0")
	@Select("select orderId,tableId,dishesName,num from orderinfo as oi join orderdishes as od on oi.orderId = od.orderReference join dishesinfo as di on od.dishes = di.dishesid where completedState=0")
	public List<OrderInfo> getOrderByCompletedState();

	@Update("update orderinfo set completedState=1 where orderId=#{orderId}")
	public int updateOrderState(@Param("orderId")int orderId);

	@Select("select count(*) from orderinfo where completedState=#{completedState}")
	public Long getMaxPageByState(@Param("completedState") int completedState);

	@Select("select orderId,tableId,dishesName,num from orderinfo as oi join orderdishes as od on oi.orderId = od.orderReference join dishesinfo as di on od.dishes = di.dishesid where completedState=0 order by orderId limit #{first},#{max}")
	public List<OrderInfo> getOrderByPage(@Param("first") int first, @Param("max") int max);

	@Select("SELECT month(a.orderEndDate) as stime,SUM(d.dishesPrice*od.num) as ordersum FROM orderinfo a,dishesinfo d,orderdishes od WHERE a.orderId=od.orderReference AND od.dishes=d.dishesId and a.orderEndDate between date_sub(now(),interval '7' month) and now() group by month(a.orderEndDate) order by stime")
	public List<OrderSumInfo> getOrderSum();
}
