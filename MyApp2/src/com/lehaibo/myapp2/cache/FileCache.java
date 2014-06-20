package com.lehaibo.myapp2.cache;

import java.io.File;

import com.lehaibo.myapp2.util.FileHelper;

import android.content.Context;

public class FileCache extends AbstractFileCache {

	public FileCache(Context context) {
		super(context);

	}

	@Override
	public String getSavePath(String url) {
		String filename = String.valueOf(url.hashCode());
		return getCacheDir() + filename;
	}

	@Override
	public String getCacheDir() {

		return FileHelper.getCacheFileSavePath();
	}

}
