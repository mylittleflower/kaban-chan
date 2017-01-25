package id.bittersweet.kaban.BaseLibrary;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by mzennis on 9/19/16.
 */
public class SharedPreferenceHelper {

    private SharedPreferences sharedPreferences;

    public SharedPreferenceHelper(Context context) {
        sharedPreferences = getSharedPreference(context);
    }

    public SharedPreferences getSharedPreference(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void putString(String key, String isi) {
        sharedPreferences.edit().putString(key, isi).apply();
    }

    public void putInt(String key, int num) {
        sharedPreferences.edit().putInt(key, num).apply();
    }

    public <T> void putList(String key, List<T> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        putString(key, json);
    }

    public <T> void putObj(String key, T obj) {
        Gson gson = new Gson();
        String json = gson.toJson(obj);
        putString(key, json);
    }

    public String getString(String key) {
        return sharedPreferences.getString(key, null);
    }

    public int getInt(String key) {
        return sharedPreferences.getInt(key, 0);
    }

    public boolean getBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    public void putBoolean(String key, boolean value) {
        sharedPreferences.edit().putBoolean(key, value).apply();
    }

    public <T> List<T> getList(String key, final Class<T[]> cls) {
        Gson gson = new Gson();
        String json = getString(key);
        T[] list = gson.fromJson(json, cls);
        return list != null ? Arrays.asList(list) : null;
    }

    public <T> T getObj(String key, final Class<T> cls) {
        Gson gson = new Gson();
        String json = getString(key);
        T obj = gson.fromJson(json, cls);
        return obj != null ? obj : null;
    }

    public void logout() {
        Map<String, ?> keys = sharedPreferences.getAll();

        for (Map.Entry<String, ?> entry : keys.entrySet()) {
            sharedPreferences.edit().remove(entry.getKey()).apply();
        }
    }

    public void clear(String key) {
        sharedPreferences.edit().remove(key).apply();
    }

    public void clear() {
        sharedPreferences.edit().clear().apply();
    }

}
