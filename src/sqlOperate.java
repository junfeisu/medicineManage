package src;

import javax.swing.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

/**
 * Created by sjffly on 11/19/16.
 */
class sqlOperate {
	// 获取操作人员信息
	public static HashMap<String, String> getUser(int userId) {
		String searchUser = "select * from user where user_id = ?";
		HashMap<String, String> result = new HashMap<>();
		try {
			new connectMysql();
			PreparedStatement ps = connectMysql.getConn().prepareStatement(searchUser);
			ps.setInt(1, userId);
			ResultSet rs = ps.executeQuery();
			if (!rs.isBeforeFirst()) {
				JOptionPane.showMessageDialog(null, "There is no message of " + userId);
			} else {
				rs.next();
				result.put("username", rs.getString(1));
				result.put("user_id", rs.getString(3));
				result.put("role", rs.getString(4));
			}
			ps.close();
			connectMysql.getConn().close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	// 获取搜有人员信息
	public static ArrayList<HashMap<String, String>> getAllUsers() {
		ArrayList<HashMap<String, String>> result = new ArrayList<>();
		String message = "select * from user";
		String[] userMsg = {"user_id", "username", "role"};
		try {
			new connectMysql();
			PreparedStatement ps = connectMysql.getConn().prepareStatement(message);
			ResultSet rs = ps.executeQuery();
			if (rs.isBeforeFirst()) {
				while (rs.next()) {
					result.add(sqlOperate.createMap(rs, userMsg));
				}
			}
			ps.close();
			connectMysql.getConn().close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	// 测试sql注入
	public static ArrayList<HashMap<String, String>> test(String name) {
		ArrayList<HashMap<String, String>> result = new ArrayList<>();
		String message = "select * from test where name=" + name;
		String[] msg = {"test_id", "name"};
		try {
			new connectMysql();
			Statement stat = connectMysql.getStat();
			System.out.println("message is " + message);
			ResultSet rs = stat.executeQuery(message);
			if (rs.isBeforeFirst()) {
				while (rs.next()) {
					result.add(sqlOperate.createMap(rs, msg));
				}
			}
			stat.close();
			connectMysql.getConn().close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	// 获取某个药品的信息
	public static HashMap<String, String> getSingleMedicine(int medicineId) {
		String searchMedicine = "select * from medicine where medicine_id=?";
		String[] msg = {"medicine_id", "name", "code", "address", "description", "price", "count", "needNum", "time", "type"};
		HashMap<String, String> result = new HashMap<>();
		try {
			new connectMysql();
			PreparedStatement ps = connectMysql.getConn().prepareStatement(searchMedicine);
			ps.setInt(1, medicineId);
			ResultSet rs = ps.executeQuery();
			if (!rs.isBeforeFirst()) {
				JOptionPane.showMessageDialog(null, "There is no message of " + medicineId);
			} else {
				rs.next();
				result = sqlOperate.createMap(rs, msg);
			}
			ps.close();
			connectMysql.getConn().close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	// 获取所有药品信息(带分页功能)
	public static ArrayList getPointedMedicines(int page_num, int page_size) {
		List<HashMap<String, String>> listMap = new ArrayList<>();
		String searchMedicineByPage = "select *,(select count(*) from medicine) as total from medicine limit ?,?";
		String[] msg = {"medicine_id", "name", "code", "address", "description", "price", "count", "needNum", "time", "type", "total"};
		try {
			new connectMysql();
			PreparedStatement ps = connectMysql.getConn().prepareStatement(searchMedicineByPage);
			ps.setInt((int) 1, (page_num - 1) * page_size);
			ps.setInt((int) 2, page_size);
			ResultSet rs = ps.executeQuery();
			if (!rs.isBeforeFirst()) {
				JOptionPane.showMessageDialog(null, "there is no medicine in page " + page_num);
			} else {
				while (rs.next()) {
					listMap.add(sqlOperate.createMap(rs, msg));
				}
			}
			ps.close();
			connectMysql.getConn().close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return (ArrayList) listMap;
	}

	// 删除药品的接口
	public static void deleteMedicine(int[] medicineId) {
		int length = medicineId.length;
		if (length == 0) {
			JOptionPane.showMessageDialog(null, "the name of medicine wait to delete can't be null");
			return;
		}
		try {
			new connectMysql();
			List<Integer> results = new ArrayList<>();
			for (int i = 0; i < length; i++) {
				String deleteMedicine = "delete from medicine where medicine_id=?";
				PreparedStatement connectStat = connectMysql.getConn().prepareStatement(deleteMedicine);
				connectStat.setInt(1, medicineId[i]);
				int num = connectStat.executeUpdate();
				if (num == 0) {
					results.add(medicineId[i]);
				}
				connectStat.close();
			}
			connectMysql.getConn().close();
			if (results.size() == 0) {
				JOptionPane.showMessageDialog(null, "delete success");
			} else {
				JOptionPane.showMessageDialog(null, results + "delete failed");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 更改药品信息
	public static void updateMedicine(HashMap<String, String> msg, int medicineId) {
		Iterator<Map.Entry<String, String>> entries = msg.entrySet().iterator();
		String message = "update medicine set ";
		ArrayList<String> values = new ArrayList<>();
		while (entries.hasNext()) {
			Map.Entry entry = entries.next();
			message += entry.getKey() + "=?,";
			values.add(String.valueOf(entry.getValue()));
		}
		// to delete the last comma
		message = message.substring(0, message.length() - 1);
		message += "where medicine_id=?";
		try {
			new connectMysql();
			PreparedStatement stat = connectMysql.getConn().prepareStatement(message);
			for (int i = 0; i < values.size(); i++) {
				stat.setString(i + 1, values.get(i));
			}
			stat.setInt(values.size() + 1, medicineId);
			int num = stat.executeUpdate();
			if (num != 0) {
				JOptionPane.showMessageDialog(null, "update success");
			} else {
				JOptionPane.showMessageDialog(null, "update failed");
			}
			stat.close();
			connectMysql.getConn().close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 增加新药品
	public static HashMap<String, String> insertMedicine(String[] msg) {
		HashMap<String, String> result = new HashMap<>();
		java.lang.String insertMessage = "insert into medicine"
				+ "(name, code, address, description, price, count, needNum, time, type) values"
				+ "(?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try {
			new connectMysql();
			PreparedStatement connectStat = connectMysql.getConn().prepareStatement(insertMessage);
			connectStat.setString(1, msg[0]);
			connectStat.setString(2, msg[1]);
			connectStat.setString(3, msg[2]);
			connectStat.setString(4, msg[3]);
			connectStat.setFloat(5, Float.parseFloat(msg[4]));
			connectStat.setInt(6, Integer.parseInt(msg[5]));
			connectStat.setInt(7, Integer.parseInt(msg[6]));
			connectStat.setString(8, msg[7]);
			connectStat.setString(9, msg[8]);
			int num = connectStat.executeUpdate();
			if (num == 0) {
				result.put("error", "新增药品失败");
			} else {
				result.put("success", "新增药品成功");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	// 搜索药品
	public static ArrayList<HashMap<String, String>> searchMedicine(String searchMsg) {
		List<HashMap<String, String>> result = new ArrayList<>();
		String msg = "select *, (select count(*) from medicine where name like ?) as total from medicine where name like ?";
		String[] message = {"medicine_id", "name", "code", "address", "description", "price", "count", "needNum", "time", "type", "total"};
		try {
			new connectMysql();
			PreparedStatement ps = connectMysql.getConn().prepareStatement(msg);
			ps.setString((int) 1, "%" + searchMsg + "%");
			ps.setString((int) 2, "%" + searchMsg + "%");
			System.out.println(msg);
			ResultSet rs = ps.executeQuery();
			if (!rs.isBeforeFirst()) {
				rs.next();
				result.add(sqlOperate.createMap(rs, message));				
			} else {
				while (rs.next()) {
					result.add(sqlOperate.createMap(rs, message));
				}
				ps.close();
				connectMysql.getConn().close();
			}
			ps.close();
			connectMysql.getConn().close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return (ArrayList<HashMap<String, String>>) result;
	}

	// 获得某个销售人员的销售所有药品(带分页功能)
	public static ArrayList getSoleMedicineByUser(int userId, int page_num, int page_size) {
		ArrayList<HashMap<String, String>> result = new ArrayList<>();
		String[] msg = {"medicine_id", "name", "code", "address", "description", "price", "count", "needNum", "time", "type", "total"};
		String searchMessage = "select *,(select count(*) from sale where user_id=?) as total from medicine " +
				"where medicine_id in (select medicine_id from sale where user_id=?) limit ?,?";
		try {
			new connectMysql();
			PreparedStatement ps = connectMysql.getConn().prepareStatement(searchMessage);
			ps.setInt(1, userId);
			ps.setInt(2, userId);
			ps.setInt((int) 3, (page_num - 1) * page_size);
			ps.setInt((int) 4, page_size);
			ResultSet rs = ps.executeQuery();
			if (!rs.isBeforeFirst()) {
				HashMap<String, String> error = new HashMap<>();
				error.put("error", "There is no medicine");
				result.add(error);
			} else {
				while (rs.next()) {
					result.add(sqlOperate.createMap(rs, msg));
				}
			}
			ps.close();
			connectMysql.getConn().close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	// 获得所有销售出去的药品(带分页功能)
	public static ArrayList getSoleMedicines(int page_num, int page_size) {
		ArrayList<HashMap<String, String>> result = new ArrayList<>();
		String message = "select medicine.medicine_id,medicine.name,user.username,sale.time,sale.num," +
				"(select count(*) from sale) as total from medicine left join sale on sale.medicine_id=" +
				"medicine.medicine_id left join user on user.user_id=sale.user_id where sale.medicine_id" +
				" in (select medicine_id from sale) limit ?,?";
		String[] msg = {"medicine_id", "name", "username", "time", "num", "total"};
		try {
			new connectMysql();
			PreparedStatement ps = connectMysql.getConn().prepareStatement(message);
			ps.setInt((int) 1, (page_num - 1) * page_size);
			ps.setInt((int) 2, page_size);
			ResultSet rs = ps.executeQuery();
			if (!rs.isBeforeFirst()) {
				HashMap<String, String> error = new HashMap<>();
				error.put("error", "there is no sole medicines");
				result.add(error);
			} else {
				while (rs.next()) {
					result.add(sqlOperate.createMap(rs, msg));
				}
			}
			ps.close();
			connectMysql.getConn().close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	// 判断该销售人员是否销售过该药品
	public static Boolean isExistInSale(int user_id, int medicine_id) {
		Boolean result = false;
		String message = "select * from sale where medicine_id=? and user_id=?";
		try {
			new connectMysql();
			PreparedStatement ps = connectMysql.getConn().prepareStatement(message);
			ps.setInt(1, medicine_id);
			ps.setInt(2, user_id);
			ResultSet rs = ps.executeQuery();
			if (!rs.isBeforeFirst()) {
				result = false;
			} else {
				result = true;
			}
			ps.close();
			connectMysql.getConn().close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	// 库存预警
	public static ArrayList<HashMap<String, String>> warningMedicine() {
		ArrayList<HashMap<String, String>> result = new ArrayList<>();
		String message = "select * from medicine where count < 5 order by count limit 0,8;";
		String[] msg = {"medicine_id", "name", "code", "address", "description", "price", "count", "needNum", "time", "type"};
		try {
			new connectMysql();
			PreparedStatement ps = connectMysql.getConn().prepareStatement(message);
			ResultSet rs = ps.executeQuery();
			if (rs.isBeforeFirst()) {
				while (rs.next()) {
					result.add(sqlOperate.createMap(rs, msg));
				}
			}
			ps.close();
			connectMysql.getConn().close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	// 销售药品(参数分别为销售人员id, 药品id, 该次销售的药品数量)
	public static HashMap<String, String> saleMedicine(int user_id, int medicine_id, int saleNum) {
		HashMap<String, String> result = new HashMap<>();
		Boolean isExist = sqlOperate.isExistInSale(user_id, medicine_id);
		String message = "";
		try {
			new connectMysql();
			if (isExist) {
				message = "update sale set num = num + ? where user_id=? and medicine_id=?";
			} else {
				System.out.println("not exist");
				message = "insert into sale (num, user_id, medicine_id) values (?, ?, ?)";
			}
			PreparedStatement ps = connectMysql.getConn().prepareStatement(message);
			ps.setInt(1, saleNum);
			ps.setInt(2, user_id);
			ps.setInt(3, medicine_id);
			int resultNum = ps.executeUpdate();
			if (resultNum == 0) {
				result.put("error", "购买失败");
			} else {
				result.put("success", "购买成功");
				int[] ids = {medicine_id};
				int[] nums = {-saleNum};
				sqlOperate.stockMedicine(ids, nums);
			}
			ps.close();
			connectMysql.getConn().close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	// 获得销售药品的占比
	public static ArrayList<HashMap<String, String>> getSoleByType() {
		ArrayList<HashMap<String, String>> result = new ArrayList<>();
		String message = "select medicine.type, sum(sale.num) as num from sale left join " +
				"medicine on medicine.medicine_id=sale.medicine_id group by medicine.type";
		String[] percentMsg = {"type", "num"};
		try {
			new connectMysql();
			PreparedStatement ps = connectMysql.getConn().prepareStatement(message);
			ResultSet rs = ps.executeQuery();
			if (rs.isBeforeFirst()) {
				while (rs.next()) {
					result.add(sqlOperate.createMap(rs, percentMsg));
				}
			}
			ps.close();
			connectMysql.getConn().close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	// 获得销售金额按时间
	public static ArrayList<HashMap<String, String>> getSoleByTime() {
		ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
		String message = "select sum(sale.num) as num, medicine.price from sale left join" +
				" medicine on sale.medicine_id=medicine.medicine_id where sale.time like ?" +
				" GROUP BY sale.medicine_id";
		try {
			new connectMysql();
			PreparedStatement ps = connectMysql.getConn().prepareStatement(message);
			for (int i = 0; i < 12; i++) {
				String tmp = String.valueOf(i + 1);
				if (tmp.length() == 1) {
					tmp = "0" + tmp;
				}
				ps.setString((int) 1, "%2016-" + tmp + "%");
				ResultSet rs = ps.executeQuery();
				result.add(sqlOperate.getCount(rs, tmp));
			}
			ps.close();
			connectMysql.getConn().close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	// 获得所有的药品类型(带分页功能)
	public static ArrayList getTypes(int page_num, int page_size) {
		ArrayList<HashMap<String, String>> result = new ArrayList<>();
		String[] msg = {"type_id", "description", "typeName", "createTime", "shortDesc", "total"};
		String message = "select *, (select count(*) from medicineType) as total from medicineType limit ?,?";
		try {
			new connectMysql();
			PreparedStatement ps = connectMysql.getConn().prepareStatement(message);
			ps.setInt((int) 1, (page_num - 1) * page_size);
			ps.setInt((int) 2, page_size);
			ResultSet rs = ps.executeQuery();
			if (!rs.isBeforeFirst()) {
				HashMap<String, String> error = new HashMap<>();
				error.put("error", "there is no types");
				result.add(error);
			} else {
				while (rs.next()) {
					result.add(sqlOperate.createMap(rs, msg));
				}
			}
			ps.close();
			connectMysql.getConn().close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	// 获得具体某类型的所有药品(带分页功能)
	public static ArrayList getTypeMedicines(int page_num, int page_size, String type) {
		ArrayList<HashMap<String, String>> result = new ArrayList<>();
		String[] msg = {"medicine_id", "name", "code", "address", "description", "price", "count", "needNum", "time", "type", "total"};
		String message = "select *,(select count(*) from medicine where type=?) as total from medicine where type=? limit ?,?";
		try {
			new connectMysql();
			PreparedStatement ps = connectMysql.getConn().prepareStatement(message);
			ps.setString(1, type);
			ps.setString(2, type);
			ps.setInt((int) 3, (page_num - 1) * page_size);
			ps.setInt((int) 4, page_size);
			ResultSet rs = ps.executeQuery();
			if (!rs.isBeforeFirst()) {
				HashMap<String, String> error = new HashMap<>();
				error.put("error", "there is no medicines of this type");
				result.add(error);
			} else {
				while (rs.next()) {
					result.add(sqlOperate.createMap(rs, msg));
				}
			}
			ps.close();
			connectMysql.getConn().close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	// 进货后更新medicine
	public static HashMap<String, String> stockMedicine(int[] medicine_ids, int[] nums) {
		HashMap<String, String> result = new HashMap<>();
		String message = "update medicine set count = count + ?, needNum = needNum + ? where medicine_id=?";
		Boolean isSuccess = true;
		try {
			new connectMysql();
			PreparedStatement ps = connectMysql.getConn().prepareStatement(message);
			for (int i = 0; i < medicine_ids.length; i++) {
				ps.setInt(1, nums[i]);
				ps.setInt(2, -nums[i]);
				ps.setInt(3, medicine_ids[i]);
				int resultNum = ps.executeUpdate();
				if (resultNum == 0) {
					isSuccess = false;
					break;
				}
			}
			if (isSuccess) {
				result.put("success", "进货成功");
			} else {
				result.put("error", "进货失败");
			}
			ps.close();
			connectMysql.getConn().close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	// 进货操作
	public static HashMap<String, String> purchaseMedicine(int[] medicine_ids, int[] nums, String[] types) {
		HashMap<String, String> result = new HashMap<>();
		ArrayList<Integer> errorId = new ArrayList<Integer>();
		String message = "insert into purchase (medicine_id, num, type) values (?, ?, ?)";
		try {
			new connectMysql();
			PreparedStatement ps = connectMysql.getConn().prepareStatement(message);
			for (int i = 0; i < medicine_ids.length; i++) {
				ps.setInt(1, medicine_ids[i]);
				ps.setInt(2, nums[i]);
				ps.setString(3, types[i]);
				int num = ps.executeUpdate();
				if (num == 0) {
					errorId.add(medicine_ids[i]);
				}
			}
			if (errorId.size() == 0) {
				sqlOperate.stockMedicine(medicine_ids, nums);
				result.put("success", "purchase success");
			} else {
				result.put("error", "purchase with some error in" + errorId);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	// 按时间获取采购记录
	public static ArrayList<HashMap<String, String>> getPurchaseByTime() {
		ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
		String message = "select purchase.medicine_id, sum(purchase.num) as num, " +
				"medicine.price from purchase left join medicine on purchase.medicine_id" +
				"=medicine.medicine_id where purchase.time like ? GROUP BY purchase.medicine_id";
		try {
			new connectMysql();
			PreparedStatement ps = connectMysql.getConn().prepareStatement(message);
			for (int i = 0; i < 12; i++) {
				String tmp = String.valueOf(i + 1);
				if (tmp.length() == 1) {
					tmp = "0" + tmp;
				}
				ps.setString((int) 1, "%2016-" + tmp + "%");;
				ResultSet rs = ps.executeQuery();
				result.add(sqlOperate.getCount(rs, tmp));
			}
			ps.close();
			connectMysql.getConn().close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	// 按类别获取采购需求量占比
	public static ArrayList<HashMap<String, String>> getPurchaseByType() {
		ArrayList<HashMap<String, String>> result = new ArrayList<>();
		String message = "select type, sum(num) as num from purchase where type" +
				" in (select typeName from medicineType) GROUP BY type order by num desc";
		String[] msg = {"type", "num"};
		try {
			new connectMysql();
			PreparedStatement ps = connectMysql.getConn().prepareStatement(message);
			ResultSet rs = ps.executeQuery();
			if (rs.isBeforeFirst()) {
				while (rs.next()) {
					result.add(sqlOperate.createMap(rs, msg));
				}
			}
			ps.close();
			connectMysql.getConn().close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	// 生成对应的返回结果
	public static HashMap<String, String> createMap(ResultSet rs, String[] msg) {
		HashMap<String, String> tmp = new HashMap<>();
		try {
			for (int i = 0; i < msg.length; i++) {
				tmp.put(msg[i], rs.getString(msg[i]));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tmp;
	}

	// 获得总金额
	public static HashMap<String, String> getCount(ResultSet rs, String time) {
		HashMap<String, String> tmpResult = new HashMap<>();
		tmpResult.put("time", "2016-" + time);
		try {
			if (rs.isBeforeFirst()) {
				float count = 0;
				while (rs.next()) {
					count += rs.getInt("num") * rs.getFloat("price");
				}
				tmpResult.put("count", String.valueOf(count));
			} else {
				tmpResult.put("count", "0");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tmpResult;
	}
}