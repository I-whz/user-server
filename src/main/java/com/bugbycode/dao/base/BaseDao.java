package com.bugbycode.dao.base;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mybatis.spring.support.SqlSessionDaoSupport;

public class BaseDao extends SqlSessionDaoSupport {

	private final Logger logger = LogManager.getLogger(BaseDao.class);
	
	@Resource(name="sqlSessionFactory")
	public void initSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		super.setSqlSessionFactory(sqlSessionFactory);
		logger.info("Init sqlsession factory......");
	}
}