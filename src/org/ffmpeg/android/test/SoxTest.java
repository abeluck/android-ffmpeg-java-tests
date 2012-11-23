package org.ffmpeg.android.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import net.sourceforge.sox.CrossfadeCat;
import net.sourceforge.sox.SoxController;
import android.content.Context;
import android.test.AndroidTestCase;
import android.util.Log;

/**
 * To run this test you must place two wav files on your sdcard:
 * /sdcard/one.wav
 * /sdcard/two.wav
 */
public class SoxTest extends AndroidTestCase {
	SoxController mController;
	File tempDir = null;

    private void deleteDirectory(File dir) {
        for(File child : dir.listFiles() ) {
                if( child.isDirectory() ) {
                        deleteDirectory(child);
                        child.delete();
                }
                else
                        child.delete();
        }
        dir.delete();
    }

    public String randomFileName() {
		return tempDir.getAbsolutePath() + File.pathSeparator + Integer.toString((int) (Math.random() * 1024))+".wav";
	}

	@Override
	protected void setUp() {

		try {
			mController = new SoxController(mContext);
			File cacheDir = mContext.getExternalCacheDir() ;
			tempDir = new File( cacheDir.getAbsolutePath() + "/sox_test/");
			tempDir.mkdir();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void tearDown() {
		deleteDirectory(tempDir);
	}

	public void testLength() {
		double length = mController.getLength("/sdcard/one.wav");
		assertEquals(7.274667, length);
	}
	public void testTrim() {
		String result = mController.trimAudio("/sdcard/one.wav", "0:0:3", null, randomFileName());
		assertNotNull(result);
	}
	public void testFade() {
		//simple fade in
		String result = mController.fadeAudio("/sdcard/one.wav", "t", "3", "0", "0", randomFileName());
		assertNotNull(result);
	}
	public void testCombineMix() {
		ArrayList<String> files = new ArrayList<String>();
		files.add("/sdcard/one.wav");
		files.add("/sdcard/two.wav");
		String result = mController.combineMix(files, randomFileName());
		assertNotNull(result);
	}
	public void testCrossfadeCat() {
		CrossfadeCat fader = new CrossfadeCat(mController, "/sdcard/one.wav", "/sdcard/two.wav", 4.0, randomFileName());
		boolean result = fader.start();
		assertTrue(result);
	}

}
