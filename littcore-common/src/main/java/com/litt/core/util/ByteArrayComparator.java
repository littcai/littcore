package com.litt.core.util;

import java.util.Comparator;

/**
 * Comparator that is used to compare byte arrays. This should be used to compare
 * IP addresses using {@link java.net.InetAddress#getAddress()} and can be used to
 * compare any pair of IPv4 and/or IPv6 addresses.
 * 
 * @author Seth <seth@opennms.org>
 */
public class ByteArrayComparator implements Comparator<byte[]> {

    public int compare(byte[] a, byte[] b) {
        if (a == null && b == null) {
            return 0;
        } else if (a == null) {
            return -1;
        } else if (b == null) {
            return 1;
        } else {
            // Make shorter byte arrays "less than" longer arrays
            int comparison = new Integer(a.length).compareTo(new Integer(b.length));
            if (comparison == 0) {
                // Compare byte-by-byte
                for (int i = 0; i < a.length; i++) {
                    int byteComparison = new Long(unsignedByteToLong(a[i])).compareTo(new Long(unsignedByteToLong(b[i])));
                    if (byteComparison == 0) {
                        continue;
                    } else {
                        return byteComparison;
                    }
                }
                // OK both arrays are the same length and every byte is identical so they are equal
                return 0;
            } else {
                return comparison;
            }
        }
    }

    private static long unsignedByteToLong(byte b) {
        return b < 0 ? ((long)b)+256 : ((long)b);
    }
}