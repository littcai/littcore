package com.litt.core.log.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.litt.core.log.Appender;
import com.litt.core.log.OpLogVo;


/**
 * 操作日志数据入库实现类.
 * <pre><b>描述：</b>
 * 操作日志入库
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">空心大白菜</a>
 * @since 2008-11-21 14:57:25
 * @version 1.0
 */
public class JDBCAppender implements Appender
{ 	
	
	/** The Constant logger. */
	private static final Log logger = LogFactory.getLog(JDBCAppender.class);
	
	/** The Constant INSERT_SQL. */
	private String INSERT_SQL = "INSERT INTO OP_LOG(LOG_ID, MODULE_CODE,FUNC_CODE,FUNC_TYPE,OP_ID,OP_NAME,OP_IP,OP_DATETIME,CONTENT,STATUS) VALUES(?,?,?,?,?,?,?,?,?,?)";
	
	/** 日志缓存队列. */
	private BlockingQueue<OpLogVo> logCacheQueue;	
	
	/** 数据源. */
	private DataSource dataSource;
	
	/** 批量入库缓存数量. */
	private int bufferSize = 100;
	
	/** 自动刷新时间(毫秒). */
	private long flushTime = 30000;	
	
	/** 是否停止日志记录. */
	private boolean isStop = false;
	
	/**
	 * Instantiates a new jDBC appender.
	 * 
	 * @param logCacheQueue 日志缓存队列
	 * @param dataSource 数据源
	 */
	public JDBCAppender(BlockingQueue<OpLogVo> logCacheQueue,DataSource dataSource)
	{
		this.logCacheQueue = logCacheQueue;		
		this.dataSource = dataSource;
	}
	
	/**
	 * Instantiates a new jDBC appender.
	 * 
	 * @param logCacheQueue 日志缓存队列
	 * @param dataSource 数据源
	 * @param tableName 日志表名称
	 */
	public JDBCAppender(BlockingQueue<OpLogVo> logCacheQueue,DataSource dataSource,String tableName)
	{
		this.logCacheQueue = logCacheQueue;		
		this.dataSource = dataSource;
		INSERT_SQL = "INSERT INTO "+tableName+"(LOG_ID,MODULE_CODE,FUNC_CODE,FUNC_TYPE,OP_ID,OP_NAME,OP_IP,OP_DATETIME,CONTENT,STATUS) VALUES(?,?,?,?,?,?,?,?,?,?)";
	}

	/**
	 * 保存操作日志.
	 * 
	 * @param busiLogList 业务日志队列
	 */
	private void batchLog(List<OpLogVo> busiLogList)
	{		
		Connection conn = null;
		PreparedStatement stmt = null;
		try
		{
			conn = dataSource.getConnection();
			conn.setAutoCommit(false);
			stmt = conn.prepareStatement(INSERT_SQL);			
			for(int i=0;i<busiLogList.size();i++)
			{
				OpLogVo opLogVo = busiLogList.get(i);
				stmt.setString(1, UUID.randomUUID().toString());
				stmt.setString(2, opLogVo.getModuleCode());	
				stmt.setString(3, opLogVo.getFuncCode());	
				stmt.setInt(4, opLogVo.getFuncType());						
				stmt.setLong(5, opLogVo.getOpId()==null?0:opLogVo.getOpId());
				stmt.setString(6, opLogVo.getOpName());
				stmt.setString(7, opLogVo.getOpIp());
				stmt.setTimestamp(8, new java.sql.Timestamp(opLogVo.getOpDatetime().getTime()));
				stmt.setString(9, opLogVo.getContent());				
				stmt.setBoolean(10, opLogVo.getStatus());
				stmt.addBatch();
			}
			stmt.executeBatch();
			conn.commit();
		}
		catch (Exception e)
		{
			logger.error("Store OpLog failed.",e);  
			closeStmt(stmt);
			throw new RuntimeException("Store OpLog failed.",e);			
		}
		finally
		{
			closeStmt(stmt);
			closeConnection(conn);
		}
	}
	
	/**
	 * Close stmt.
	 * 
	 * @param stmt the stmt
	 */
	private void closeStmt(PreparedStatement stmt)
	{
		try 
		{
		   if (stmt != null)
			   stmt.close();
		} 
		catch (SQLException e) 
		{
		   logger.error("Close db statement for OpLog failed.",e);     
		}
	}	
	
	/**
	 * Close connection.
	 * 
	 * @param conn the conn
	 */
	private void closeConnection(Connection conn)
	{
		try 
		{
		   if (conn != null && !conn.isClosed())
			   conn.close();
		} 
		catch (SQLException e) 
		{
			logger.error("Close db connection for OpLog failed.",e);       
		}
	}


	/* (non-Javadoc)
	 * @see java.util.concurrent.Callable#call()
	 */
	public String call() throws Exception
	{
		if(logger.isInfoEnabled())
		{
			logger.info("Oplog record thread started.");
		}		
		while(!Thread.interrupted() && !isStop) 
		{
			try
			{
				List<OpLogVo> busiLogList = new ArrayList<OpLogVo>();
				for(int i=0;i<this.bufferSize;i++)	//每次等待满缓存条数才一次入库
				{
					OpLogVo busiLogVo = logCacheQueue.poll(flushTime, TimeUnit.MILLISECONDS);	//队列不为空时马上消费；为空时阻塞指定时间间隔
					if(busiLogVo!=null)
					{
						busiLogList.add(busiLogVo);						
					}
					else
						break;	//如果长时间没收到日志，且到了刷新时间，则直接入库					
				}
				if(busiLogList.size()>0)	//有日志存在才入库
					this.batchLog(busiLogList);
			}
			catch (InterruptedException e)
			{				
				logger.error("Oplog record thread is interrupted.",e);
			}
		}	
		if(logger.isInfoEnabled())
		{
			logger.info("Oplog record thread is finished.");
		}
		return null;
	}

	/**
	 * @return the isStop
	 */
	public boolean isStop() {
		return isStop;
	}

	/**
	 * @param isStop the isStop to set
	 */
	public void setStop(boolean isStop) {
		this.isStop = isStop;
	}

	/**
	 * @return the flushTime
	 */
	public long getFlushTime() {
		return flushTime;
	}


}