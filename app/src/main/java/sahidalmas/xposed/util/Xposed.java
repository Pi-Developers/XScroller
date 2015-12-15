package sahidalmas.xposed.util;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author Sahid Almas
 */
public class Xposed {

    private Context mContext;
    public static String XPOSED_PACKAGE = "de.robv.android.xposed.installer";
    public static String TAG_NAME = "[XScroller/AF]";
    public static String MODULE_PATH = "/data/data/"+XPOSED_PACKAGE+"/files/module.list";
    public static Xposed createXposed(Context context) {
        return construct(context);
    }
    public static Xposed createXposed(Activity activity) {
        return construct(activity.getApplicationContext());
    }

    private static Xposed construct(Context context) {
        return new Xposed(context);
    }

    private Xposed(Context _context) {
        synchronized (this) {
            this.mContext = _context;
        }
    }
    public  synchronized boolean isMyXposedModuleActive() {
        try {
            return isXposedModuleActive(mContext.getPackageName());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    private synchronized boolean isXposedModuleActive(String packageName) throws IOException {

        boolean result = false;
        File file = new File(MODULE_PATH);
        if (!file.exists()) {
            Log.wtf(TAG_NAME,"Something wrong in your xposed because we did'nt find module.list " +
                    "file on your device contact the developers of xposed module");
            return false;
        } else {

            try {
                BufferedReader br = new BufferedReader(new FileReader(MODULE_PATH));
                StringBuilder sb = new StringBuilder();
                String line = br.readLine();

                while (line != null) {
                    sb.append(line);
                    sb.append("\n");
                    line = br.readLine();
                }
                String everything = sb.toString();
                for (String l : everything.split("\n")) {
                    if (l.contains(packageName) || l.equalsIgnoreCase(packageName)) {
                        result = true;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }




}
