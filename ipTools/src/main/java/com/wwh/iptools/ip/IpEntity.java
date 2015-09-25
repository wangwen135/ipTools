package com.wwh.iptools.ip;

/**
 * <pre>
 * 
 * </pre>
 *
 * @author wwh
 * @date 2015年9月25日 下午2:12:30
 *
 */
public class IpEntity {
    private String ipAddress;
    private String location;

    @Override
    public String toString() {
        return "IpEntity [ipAddress=" + ipAddress + ", location=" + location + "]";
    }

    /**
     * 获取 ipAddress
     *
     * @return the ipAddress
     */
    public String getIpAddress() {
        return ipAddress;
    }

    /**
     * 设置 ipAddress
     *
     * @param ipAddress
     *            the ipAddress to set
     */
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    /**
     * 获取 location
     *
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * 设置 location
     *
     * @param location
     *            the location to set
     */
    public void setLocation(String location) {
        this.location = location;
    }

}
