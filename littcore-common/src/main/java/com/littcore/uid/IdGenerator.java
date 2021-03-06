package com.littcore.uid;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * Twitter的ID生成器.
 * 
 * <pre><b>描述：</b>
 *    64位的LONG型，相比UUID的存储和查询性能好
 * </pre>
 * 
 * <pre><b>修改记录：</b>
 *    
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">蔡源</a>
 * @since 2015年5月6日
 * @version 1.0
 */
public class IdGenerator {

  private final long workerId;

  private final static long twepoch = 1361753741828L;

  private long sequence = 0L;

  private final static long workerIdBits = 4L;

  public final static long maxWorkerId = -1L ^ -1L << workerIdBits;

  private final static long sequenceBits = 10L;

  private final static long workerIdShift = sequenceBits;

  private final static long timestampLeftShift = sequenceBits + workerIdBits;

  public final static long sequenceMask = -1L ^ -1L << sequenceBits;

  private long lastTimestamp = -1L;

  /**
   * 核心代码就是毫秒级时间41位+机器ID 10位+毫秒内序列12位 0---0000000000 0000000000 0000000000 0000000000 0 --- 00000 ---00000 ---0000000000
   * 00 在上面的字符串中，第一位为未使用（实际上也可作为long的符号位），接下来的41位为毫秒级时间，然后5位datacenter标识位，5位机器ID（并不算标识符，实际是为线程标识），然后12位该毫秒内的当前毫秒内的计数，
   * 加起来刚好64位，为一个Long型。
   * 这样的好处是，整体上按照时间自增排序，并且整个分布式系统内不会产生ID碰撞（由datacenter和机器ID作区分），并且效率较高，经测试，snowflake每秒能够产生26万ID左右，完全满足需要。
   * 
   * @param workerId
   */
  public IdGenerator(final long workerId)
  {
    super();
    if (workerId > maxWorkerId || workerId < 0)
    {
      throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", this.maxWorkerId));
    }
    this.workerId = workerId;
  }

  public synchronized long nextId()
  {
    long timestamp = this.timeGen();
    if (this.lastTimestamp == timestamp)
    {
      this.sequence = (this.sequence + 1) & sequenceMask;
      if (this.sequence == 0)
      {
        timestamp = this.tilNextMillis(this.lastTimestamp);
      }
    } else
    {
      this.sequence = 0;
    }
    if (timestamp < this.lastTimestamp)
    {
      try
      {
        throw new Exception(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", this.lastTimestamp - timestamp));
      } catch (Exception e)
      {
        e.printStackTrace();
      }
    }

    this.lastTimestamp = timestamp;
    long nextId = ((timestamp - twepoch << timestampLeftShift)) | (this.workerId << this.workerIdShift) | (this.sequence);
    // System.out.println("timestamp:" + timestamp + ",timestampLeftShift:"
    // + timestampLeftShift + ",nextId:" + nextId + ",workerId:"
    // + workerId + ",sequence:" + sequence);
    return nextId;
  }

  private long tilNextMillis(final long lastTimestamp)
  {
    long timestamp = this.timeGen();
    while (timestamp <= lastTimestamp)
    {
      timestamp = this.timeGen();
    }
    return timestamp;
  }

  private long timeGen()
  {
    return System.currentTimeMillis();
  }

  public static void main(String[] args)
  {
    IdGenerator worker1 = new IdGenerator(1);
    IdGenerator worker2 = new IdGenerator(2);
    long start = System.currentTimeMillis();
    Map<Long, Long> map = new HashMap<Long, Long>();
    for (long i = 0; i < 100; i++)
    {
      System.out.println(worker2.nextId());
      map.put(worker2.nextId(), worker2.nextId());
    }
    for (long i = 0; i < 100; i++)
    {
      //System.out.println(worker2.nextId());
      map.put(worker1.nextId(), worker1.nextId());
    }
    System.out.println(System.currentTimeMillis() - start);
    System.out.println(map.size());
  }

}
