package com.littcore.net.ftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import com.littcore.io.util.IOUtils;

/** 
 * 
 * FTP客户端辅助类.
 * 
 * <pre><b>描述：</b>
 *    TODO 
 * </pre>
 * 
 * <pre><b>修改记录：</b>
 *    
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">蔡源</a>
 * @since 2009-3-25
 * @version 1.0
 *
 */
public class FtpClientTool
{
	private static final Log logger = LogFactory.getLog(FtpClientTool.class);
	
	/**
	 * 采集成功
	 */
	public static final int DOWNLOAD_STATUS_SUCCESS = 1;
	
	/**
	 * 采集失败
	 */
	public static final int DOWNLOAD_STATUS_FAIL = 2;	
	
	/**
	 * 本地已存在该文件，且已经采集完成
	 */
	public static final int DOWNLOAD_STATUS_LOCAL_EXIST = 3;	
	
	/**
	 * 本地已存在该文件，且文件大小大于需要采的文件
	 */
	public static final int DOWNLOAD_STATUS_LOCAL_BIGGER = 4;	
	
	/**
	 * 远程文件不存在
	 */
	public static final int DOWNLOAD_STATUS_FILE_NOT_FOUND = 5;	
	
	/**
	 * 网络异常
	 */
	public static final int DOWNLOAD_STATUS_IO_EXCEPTION = 6;	
	

	/** FTP服务器地址. */
	private String serverAddress;

	/** FTP服务器端口号. */
	private int port = FTPClient.DEFAULT_PORT;

	/** 控制编码. */
	private String controlEncoding;

	/** 用户名. */
	private String userName;

	/** 密码. */
	private String password;

	/** 本地保存路径. */
	private String localPath;

	/** 远端路径. */
	private String remotePath = "..";

	/** 是否显示调用命令. */
	private boolean isShowCmd = false;

	private FTPClient ftpClient;
	
	/** 已下载大小（超大文件使用）. */
	private long downloadedSize;

	public FtpClientTool(String serverAddress)
	{
		super();
		this.serverAddress = serverAddress;
	}

	/**
	 * 连接FTP
	 * @throws IOException
	 */
	public void connect() throws IOException
	{
		this.ftpClient = new FTPClient();
		
		//if(isShowCmd)
		//   client.addProtocolCommandListener( new  PrintCommandListener( new  PrintWriter(System.out)));   

		ftpClient.connect(serverAddress, port);
		//ftpClient.setControlEncoding(controlEncoding);
		// check whether the connection to server is confirmed   
		int reply = ftpClient.getReplyCode();
		if (!FTPReply.isPositiveCompletion(reply))
		{
			ftpClient.disconnect();
			throw new IOException("failed to connect to the FTP Server:" + serverAddress);
		}
		if (logger.isDebugEnabled())
		{
			logger.debug("connect to FTP SERVER:" + serverAddress + "  success.");
		}		
	}

	/**
	 * 登录
	 * @return 成功标志
	 * @throws IOException
	 */
	public boolean login() throws IOException
	{
		if (logger.isDebugEnabled())
		{
			logger.debug("try to login... userName:" + userName);
		}
		boolean ret = ftpClient.login(userName, password);
		if (logger.isDebugEnabled())
		{
			logger.debug("login success.");
		}
		//设置被动模式    
		ftpClient.enterLocalPassiveMode();
		//设置以二进制方式传输    
		ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
		return ret;
	}

	/**
	 * 注销登录
	 * @return 成功标志
	 * @throws IOException
	 */
	public boolean logout() throws IOException
	{
		if (logger.isDebugEnabled())
		{
			logger.debug("login out.");
		}
		return ftpClient.logout();
	}
	
	/**
	 * 是否连接状态.
	 * 
	 * @return true, if checks if is connected
	 */
	public boolean isConnected()
	{
		if(this.ftpClient == null || !ftpClient.isConnected())
			return false;
		else
			return true;
	}

	/**
	 * 断开连接.
	 * 
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void disconnect() throws IOException
	{
		if (ftpClient.isConnected())
		{
			ftpClient.disconnect();
		}
	}

	/**
	 * 断开连接（异常不报错）.
	 * 
	 */
	public void disconnectQuietly()
	{
		try
		{
			if (ftpClient.isConnected())
			{
				ftpClient.disconnect();
			}
		}
		catch (IOException e)
		{
			logger.error("断开FTP连接异常，静默消息",e);
		}
	}

	/**
	 * 创建远程目录.
	 * 
	 * @param dirName 目录名称
	 * 
	 * @return true, if make remote dir
	 * 
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public boolean makeRemoteDir(String dirName) throws IOException
	{
		boolean isSuccess = ftpClient.makeDirectory(charsetEncode(dirName));
		if(logger.isInfoEnabled())
		{
			if(isSuccess)
				logger.info("创建远程文件夹："+dirName);
		}
		return isSuccess;
	}
	
	/**
	 * 删除远程目录.
	 * 
	 * @param dirName 目录名称
	 * 
	 * @return true, if delete remote dir
	 * 
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public boolean deleteRemoteDir(String dirName) throws IOException
	{
		String encodedDirName = charsetEncode(dirName);
		boolean isSuccess = ftpClient.removeDirectory(encodedDirName);		
		if(!isSuccess)	//如果不成功，可能是该目录下有文件存在，需要先删除文件
		{
			boolean isChanged = ftpClient.changeWorkingDirectory(encodedDirName);
			if(!isChanged)	//目录没有更改成功，可能为目录不存在，则直接返回
			{
				throw new IOException("远程文件夹不存在！");
			}
			FTPFile[] files = ftpClient.listFiles();
			for(int i=0;i<files.length;i++)
			{
				FTPFile file = files[i];
				String fileName = charsetDecode(file.getName());
				
				if(file.isFile())
				{
					boolean isDeleted = ftpClient.deleteFile(charsetEncode(fileName));
					if(isDeleted)
					{
						if(logger.isInfoEnabled())
						{
							logger.info("删除远程文件："+fileName);
						}
					}
				}
				else if(file.isDirectory())
				{
					if(".".equals(fileName)||"..".equals(fileName))
						continue;
					isSuccess = deleteRemoteDir(fileName);	//递归删除					
				}
			}
			isChanged = ftpClient.changeToParentDirectory();	//需返回父文件夹才能删除自己
			if(!isChanged)	//目录没有更改成功，可能为目录不存在，则直接返回
			{
				throw new IOException("返回父文件夹失败！");
			}
			isSuccess = ftpClient.removeDirectory(encodedDirName);
			if(isSuccess)
			{
				if(logger.isInfoEnabled())
				{
					logger.info("删除远程文件夹："+dirName);
				}
			}
		}
		else
		{
			if(logger.isInfoEnabled())
			{
				logger.info("删除远程文件夹："+dirName);
			}
		}
		return isSuccess;
	}	
	
	/**
	 * 心跳，并可根据返回值检测状态
	 * @return
	 * @throws IOException
	 */
	public int noop() throws IOException
	{
		return ftpClient.noop();
	}
	
	/**
	 * 心跳，并可根据返回值检测状态
	 * @return
	 * @throws IOException
	 */
	public boolean sendNoOp() throws IOException
	{
		return ftpClient.sendNoOp();
	}	
	
	/**
	 * 设置超时.
	 * @param timeout
	 */
	public void setTimeout(int timeout)
	{
		ftpClient.setDefaultTimeout(timeout);
	}
	
	/**
	 * 切换工作目录.
	 * 
	 * @param dirName 目录名称
	 * 
	 * @return true, if change working directory
	 * 
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public boolean changeWorkingDirectory(String dirName) throws IOException
	{
		return ftpClient.changeWorkingDirectory(charsetEncode(dirName));
	}
	
	/**
	 * 切换工作目录到父目录.
	 * 
	 * @return true, if change to parent directory
	 * 
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public boolean changeToParentDirectory() throws IOException
	{
		return ftpClient.changeToParentDirectory();
	}	
	
	/**
	 * 下载文件(默认按1M分割).
	 * 
	 * @param fileName 文件名称
	 * 
	 * @return 采集状态
	 * 
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public int download(String fileName) throws IOException
	{
		return this.download(fileName, 0);
	}

	/**
	 * 下载文件(支持断点续传).
	 * 
	 * @param fileName 文件名称
	 * @param downloadSize 需要下载的大小（超出此大小则停止下载，小于等于0时无限制）
	 * 
	 * @return true, if download
	 * 
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public int download(String fileName, long downloadSize) throws IOException
	{
		int retStatus = 0;
		//检查远程文件是否存在    
		FTPFile[] files = ftpClient.listFiles(charsetEncode(fileName));
		if (files.length != 1)
		{
			logger.warn("远程文件不存在！");
			return DOWNLOAD_STATUS_FILE_NOT_FOUND;
		}
		FTPFile remoteFile = files[0];
		long remoteFileSize = remoteFile.getSize();
		if(logger.isDebugEnabled())
		{
			logger.debug("远程文件大小："+remoteFileSize);
		}
		File localFile = new File(this.localPath, fileName);
		//本地存在文件，进行断点下载    
		if (localFile.exists())
		{
			long localSize = localFile.length();
			//判断本地文件大小是否大于远程文件大小    
			if (localSize >= remoteFileSize)
			{
				logger.warn("本地文件大于等于远程文件，下载中止！");
				return DOWNLOAD_STATUS_LOCAL_BIGGER;
			}
			// 进行断点续传，并记录状态    
			FileOutputStream out = null;
			InputStream in = null;
			try
			{
				out = new FileOutputStream(localFile, true);
				ftpClient.setRestartOffset(localSize);	//定位到新的位置
				in = ftpClient.retrieveFileStream(charsetEncode(fileName));
				byte[] bytes = new byte[1024];
				long step = remoteFileSize / 100;
				long process = localSize / step;
				int c;
				long downloadedSize = 0;	//已下载大小
				while ((c = in.read(bytes)) != -1)
				{
					out.write(bytes, 0, c);
					localSize += c;
					downloadedSize +=c;
					
					if(downloadSize>0 && downloadedSize>=downloadSize)
					{
						if(logger.isDebugEnabled())
						{
							logger.debug("超出下载限制，停止下载");
						}
						break;				
					}
					if(logger.isDebugEnabled())
					{
						long nowProcess = localSize / step;
						if (nowProcess > process)
						{
							process = nowProcess;
							if (process % 10 == 0)
							{							
								logger.debug(" 下载进度： " + process + "，指针位置："+downloadedSize);
							}
						}					
					}
				}
				out.flush();
			}
			catch (FileNotFoundException e)
			{
				logger.error("本地文件不存在！", e);
			}
			catch (IOException e)
			{
				logger.error("传输异常！", e);
				retStatus = DOWNLOAD_STATUS_IO_EXCEPTION;
			}
			finally
			{
				IOUtils.closeQuietly(in);
				IOUtils.closeQuietly(out);				
			}			
			
			boolean isDone = ftpClient.completePendingCommand();
			if(isDone)
				retStatus = DOWNLOAD_STATUS_SUCCESS;
			else
				retStatus = DOWNLOAD_STATUS_FAIL;
			return retStatus;
		}
		else
		{
			OutputStream out = null;
			InputStream in = null;
			try
			{
				out = new FileOutputStream(localFile);
				in = ftpClient.retrieveFileStream(charsetEncode(fileName));
				byte[] bytes = new byte[1024];
				long step = remoteFileSize / 100;
				long process = 0;
				long downloadedSize = 0L;
				int c;
				while ((c = in.read(bytes)) != -1)
				{
					out.write(bytes, 0, c);
					downloadedSize += c;
					if(downloadSize>0 && downloadedSize>=downloadSize)
					{
						if(logger.isDebugEnabled())
						{
							logger.debug("超出下载限制，停止下载");
						}
						break;				
					}
					if(logger.isDebugEnabled())	//调试时输出下载进度及定位指针
					{				
						long nowProcess = downloadedSize / step;
						if (nowProcess > process)
						{
							process = nowProcess;
							if (process % 10 == 0)
							{
								logger.debug(" 下载进度： " + process + "，指针位置："+downloadedSize);
							}
						}
					}
				}
				out.flush();
			}
			catch (FileNotFoundException e)
			{
				logger.error("本地文件不存在！", e);
				
			}
			catch (IOException e)
			{
				logger.error("传输异常！", e);
				retStatus = DOWNLOAD_STATUS_IO_EXCEPTION;
			}
			finally
			{
				IOUtils.closeQuietly(in);
				IOUtils.closeQuietly(out);				
			}
			
			boolean isDone = ftpClient.completePendingCommand();
			if(isDone)
				retStatus = DOWNLOAD_STATUS_SUCCESS;
			else
				retStatus = DOWNLOAD_STATUS_FAIL;
			return retStatus;
		}
	}
	
	/**
	 * 上传文件到当前工作目录(不覆盖).
	 * 
	 * @param file 文件对象
	 * 
	 * @return true, if upload
	 * 
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public boolean uploadFile(File file) throws IOException
	{
		return this.uploadFile(file, false);
	}

	/**
	 * 上传文件到当前工作目录.
	 * 
	 * @param file 文件对象
	 * @param isOverwrite 是否覆盖文件(不覆盖时若服务器存在同名文件，则自动在新文件名后加1)
	 * 
	 * @return true, if upload
	 * 
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public boolean uploadFile(File file,boolean isOverwrite)
	{
		boolean isSuccess = false;
		String fileName = file.getName();
		String encodedFileName = charsetEncode(fileName);
		InputStream in = null;
		try
		{
			in = new FileInputStream(file);
			if(!isOverwrite)
				isSuccess = ftpClient.storeUniqueFile(encodedFileName, in);
			else
				isSuccess = ftpClient.storeFile(encodedFileName, in);
			if (logger.isInfoEnabled())
			{
				logger.info("上传文件结束...上传路径：" + remotePath + "，文件名：" + fileName);
			}
		}
		catch (IOException e)
		{
			logger.error("上传文件失败！", e);
		}
		finally
		{
			IOUtils.closeQuietly(in);
		}
		
		return isSuccess;
	}
	
	/**
	 * 上传目录(不覆盖).
	 * 
	 * @param dir 目录文件对象
	 * 
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void uploadDir(File dir) throws IOException
	{	
		this.uploadDir(dir,false);
	}
	
	/**
	 * 上传目录.
	 * 
	 * @param dir 目录文件对象
	 * @param isOverwrite 是否覆盖文件
	 * 
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void uploadDir(File dir,boolean isOverwrite) throws IOException
	{
		//首先在服务器上创建目录，并切换到该目录
		String dirName = dir.getName();
		this.makeRemoteDir(dirName);	//创建远程目录
		this.remotePath = dirName;		//设置远程路径为该目录
		this.changeWorkingDirectory(dirName);
		
		File[] files = dir.listFiles();
		for(int i=0;i<files.length;i++)
		{
			File file = files[i];
			if(file.isFile())	//文件
			{				
				this.uploadFile(file,isOverwrite);
			}
			else	//目录
			{
				//递归处理该目录
				uploadDir(file);
				this.changeToParentDirectory();	//处理完后返回父目录				
			}		
		}
	}
	
	/**
	 * 上传目录下文件(不覆盖).
	 * 
	 * @param dir 目录文件对象
	 * 
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void uploadDirFiles(File dir) throws IOException
	{	
		this.uploadDir(dir,false);
	}	
	
	/**
	 * 上传目录下的所有文件.
	 * 
	 * @param dir 目录文件对象
	 * @param isOverwrite 是否覆盖文件
	 * 
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void uploadDirFiles(File dir,boolean isOverwrite) throws IOException
	{		
		File[] files = dir.listFiles();
		for(int i=0;i<files.length;i++)
		{
			File file = files[i];
			if(file.isFile())	//文件
			{				
				this.uploadFile(file,isOverwrite);
			}
			else	//目录
			{
				//递归处理该目录
				uploadDir(file);						
			}		
		}
	}
	

	/**
	 * FTP协议为iso-8859-1，所以需要对中文进行转码
	 * @param name 名称
	 * @return 转码后名称
	 */
	public static String charsetEncode(String name)
	{
		try
		{
			return new String(name.getBytes(), "iso-8859-1");
		}
		catch (UnsupportedEncodingException e)
		{
			return name;
		}
	}
	
	/**
	 * FTP协议为iso-8859-1，所以需要对中文进行转码
	 * @param name 名称
	 * @return 转码后名称
	 */
	public static String charsetDecode(String name)
	{
		try
		{
			return new String(name.getBytes("iso-8859-1"));
		}
		catch (UnsupportedEncodingException e)
		{
			return name;
		}
	}	

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception
	{
		FtpClientTool util = new FtpClientTool("127.0.0.1");
		util.connect();
		util.setUserName("littcai");
		util.setPassword("123456");
		util.login();
		util.setLocalPath("C:\\");
		util.setRemotePath("\\");
		//long a = System.currentTimeMillis();
		//util.download("test.txt");
		//long b = System.currentTimeMillis() - a;
		//System.out.println(b);
		//util.makeRemoteDir("中文");
		//util.deleteRemoteDir("中文");
		boolean isSuccess = util.uploadFile(new File("D:\\install.txt"), true);
		System.out.println(isSuccess);
		util.uploadDir(new File("D:\\CODEGEN"));

	}

	/**
	 * @return the isShowCmd
	 */
	public boolean isShowCmd()
	{
		return isShowCmd;
	}

	/**
	 * @param isShowCmd the isShowCmd to set
	 */
	public void setShowCmd(boolean isShowCmd)
	{
		this.isShowCmd = isShowCmd;
	}

	/**
	 * @return the localPath
	 */
	public String getLocalPath()
	{
		return localPath;
	}

	/**
	 * @param localPath the localPath to set
	 */
	public void setLocalPath(String localPath)
	{
		this.localPath = localPath;
	}

	/**
	 * @return the password
	 */
	public String getPassword()
	{
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password)
	{
		this.password = password;
	}

	/**
	 * @return the port
	 */
	public int getPort()
	{
		return port;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(int port)
	{
		this.port = port;
	}

	/**
	 * @return the remotePath
	 */
	public String getRemotePath()
	{
		return remotePath;
	}

	/**
	 * @param remotePath the remotePath to set
	 */
	public void setRemotePath(String remotePath)
	{
		this.remotePath = charsetEncode(remotePath);
	}

	/**
	 * @return the serverAddress
	 */
	public String getServerAddress()
	{
		return serverAddress;
	}

	/**
	 * @param serverAddress the serverAddress to set
	 */
	public void setServerAddress(String serverAddress)
	{
		this.serverAddress = serverAddress;
	}

	/**
	 * @return the userName
	 */
	public String getUserName()
	{
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName)
	{
		this.userName = userName;
	}

}
