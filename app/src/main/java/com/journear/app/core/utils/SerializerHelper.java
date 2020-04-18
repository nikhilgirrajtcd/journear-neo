package com.journear.app.core.utils;

import com.journear.app.core.entities.JnGeocodeItem;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.nustaq.serialization.FSTConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class SerializerHelper {

    static FSTConfiguration _conf = null;

    private static FSTConfiguration getFstConfiguration() {
        if (_conf == null) {
            _conf = FSTConfiguration.createAndroidDefaultConfiguration();
        }
        return _conf;
    }

    public static String serialize(ArrayList<JnGeocodeItem> obj, String outFilePath, Class serializableType) {
        getFstConfiguration().registerClass(serializableType);

        if (StringUtils.isEmpty(outFilePath))
            outFilePath = "C:\\Work\\Projects\\journearneo\\app\\src\\main\\res\\raw\\geocodes_ie_fst.fst";
        try {
            byte[] barray = getFstConfiguration().asByteArray(obj);
            System.out.println("Bytes: " + barray.length);
            final File fOut = new File(outFilePath);
            final FileOutputStream fOutputStream = new FileOutputStream(fOut);
            fOutputStream.write(barray);
            fOutputStream.close();
            // read
        } catch (final Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return outFilePath;
    }


    public static Object deserialize(InputStream fstStream, Class targetClass) {
        try {
            getFstConfiguration().registerClass(targetClass);
            return getFstConfiguration().asObject(IOUtils.toByteArray(fstStream));
        } catch (final Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T copyObject(T source)
    {
        return (T) getFstConfiguration().asObject(getFstConfiguration().asByteArray(source));
    }

}
