package cn.mldn.travel.dao;

import cn.mldn.travel.vo.Level;
import cn.mldn.util.dao.IBaseDAO;

public interface ILevelDAO extends IBaseDAO<String,Level> {
	/**
	 * 根据员工的职位信息，来查询到职员的中文等级名称
	 * @param lid  员工的职位信息
	 * @return 职员的职位等级名称
	 */
	public Level findLevelByLid(String lid);
}
