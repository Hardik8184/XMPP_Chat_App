package com.oozee.xmppchat.xmpp;

import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.bytestreams.ibb.provider.DataPacketProvider;
import org.xmlpull.v1.XmlPullParser;

/**
 * Created by peacock on 27/09/16.
 */

public final class DelayExtensionProvider extends DataPacketProvider.PacketExtensionProvider {


    public DelayExtensionProvider() {
        //do nowt
    }

    /**
     * Installs the provider.
     */
    public static void install() {
        ProviderManager.addExtensionProvider("x", "jabber:x:delay", new DelayExtensionProvider());
    }

    public PacketExtension parseExtension(XmlPullParser parser) throws Exception {
        DelayExtension result = new DelayExtension();
        int c = parser.getAttributeCount();
        String name;
        String value;
        for (int i = 0; i < c; i++) {
            value = parser.getAttributeValue(i);
            name = parser.getAttributeName(i);
            if (name.equals("stamp")) {
                result.setStamp(value);
            }
            if (name.equals("from")) {
                result.setFrom(value);
            }
        }
        int event = parser.getEventType();
        while (!(event == XmlPullParser.END_TAG && parser.getName().equals("x"))) {
            if (event == XmlPullParser.TEXT) {
                result.setContent(parser.getText());
            }
            event = parser.next();
        }
        return result;
    }

}
