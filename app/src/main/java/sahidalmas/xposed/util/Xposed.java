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
    private static String XPOSED_PACKAGE = "de.robv.android.xposed.installer";
    private static String TAG_NAME = "[XScroller/AF]";
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
        File file = new File(getModuleListPath());
        if (!file.exists()) {
            Log.wtf(TAG_NAME,"Something wrong in your xposed because we did'nt find module.list " +
                    "file on your device contact the developers of xposed module");
            return false;
        } else {

            BufferedReader br = new BufferedReader(new FileReader(getModuleListPath()));
            try {
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
            } finally {
                br.close();
            }
        }
        return result;
    }

    public String getModuleListPath() {
        String result = "";
        for (File file : listf("/data/data/"+XPOSED_PACKAGE)) {
            if (file.getAbsolutePath().contains("module.list") || file.getAbsolutePath().contains("modules.list")) {
                result = file.getAbsolutePath();
            }
        }
        return result;
    }
    public File[] listf(String directoryName) {

        // .............list file
        File directory = new File(directoryName);

        // get all the files from a directory
        File[] fList = directory.listFiles();

        for (File file : fList) {
            if (file.isFile()) {
                System.out.println(file.getAbsolutePath());
            } else if (file.isDirectory()) {
                listf(file.getAbsolutePath());
            }
        }

        return fList;
    }


}
