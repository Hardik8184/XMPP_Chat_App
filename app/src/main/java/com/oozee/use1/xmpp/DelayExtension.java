package com.oozee.use1.xmpp;

import org.jivesoftware.smack.packet.PacketExtension;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Packet extension for Delayed Delivery jabber:x:delay.
 * See <a href="http://www.jabber.org/jeps/jep-0091.html">JEP 91</a>
 * for more info.
 */

public final class DelayExtension implements PacketExtension {

    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyyMMdd''T''hh:mm:ss");
    private static final DateFormat LOCAL_FORMAT = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
    private static final DateFormat LOCAL_TIME_FORMAT = DateFormat.getTimeInstance(DateFormat.SHORT);


    private String from = null;
    private String stamp = "";
    private String content = null;

    /**
     * Creates an empty DelayExtension.
     */
    public DelayExtension() {
        //do nowt
    }

    /**
     * Sets the time stamp attribute.
     */
    public void setStamp(String stamp) {
        if (stamp == null) {
            throw new NullPointerException("null stamp passed to setStamp in DelayExtension");
        }
        this.stamp = stamp;
    }

    /**
     * Sets the from attribute.
     */
    public void setFrom(String from) {
        this.from = from;
    }

    /**
     * Returns the the x tag content.
     * EG Offline Storage.
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Returns the from attribute.
     * May return <code>null</code>.
     */
    public String getFrom() {
        return from;
    }

    /**
     * Returns the the x tag content.
     * EG Offline Storage.
     * May return <code>null</code>.
     */
    public String getContent() {
        return content;
    }

    /**
     * Returns the time stamp as a string.
     * Format is yyyyMMddThh:mm:ss.
     */
    public String getStamp() {
        return stamp;
    }

    /**
     * Returns the time stamp as a date object.
     * Returns <code>null</code> if there was a problem parsing the time stamp.
     */
    public Date getDate() {
        try {
            Calendar cal = Calendar.getInstance();
            // Convert the UTC time to local time.
            cal.setTime(new Date(FORMAT.parse(stamp).getTime() + cal.getTimeZone().getOffset(cal.getTimeInMillis())));
            return cal.getTime();
        } catch (ParseException pe) {
            return null;
        }
    }

    /**
     * Returns the time stamp as a localised string.
     * Returns <code>""</code> if there was a problem parsing the time stamp.
     */
    public String getLocalStamp() {
        Date d = getDate();
        if (d == null) {
            return "";
        }
        return LOCAL_FORMAT.format(d);
    }

    /**
     * Returns the time part of the time stamp as a localised string.
     * Returns <code>""</code> if there was a problem parsing the time stamp.
     */
    public String getLocalTime() {
        Date d = getDate();
        if (d == null) {
            return "";
        }
        return LOCAL_TIME_FORMAT.format(d);
    }


    // Interface implementation

    /**
     * Returns ''x''.
     */
    public String getElementName() {
        return "x";
    }

    /**
     * Returns ''jabber:x:delay''.
     */
    public String getNamespace() {
        return "jabber:x:delay";
    }

    public String toXML() {
        StringBuffer buf = new StringBuffer();
        buf.append("<x xmlns=''jabber:x:delay'' ");
        buf.append("stamp=''").append(stamp).append("'' ");
        if (from != null) {
            buf.append("from=''").append(from).append("'' ");
        }
        buf.append(">");
        if (content != null) {
            buf.append(content);
        }
        buf.append("</x>");
        return buf.toString();
    }
}