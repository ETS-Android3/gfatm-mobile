package com.example.backupservice;

import android.content.Context;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by Owais on 8/2/2017.
 */
public class BackupAndRestoreTest {
    Context context;

    @Before
    public void setUp() throws Exception {
        context = InstrumentationRegistry.getTargetContext();
    }

    @Test
    public void takeBackup() throws Exception {
        assertTrue(new BackupAndRestore().takeBackup(context, "Practice", "//DCIM", "com.example.owais.backupservicesqlite"));
    }

    @Test
    public void takeEncryptedBackup() throws Exception {
        assertTrue(new BackupAndRestore().takeEncryptedBackup(context, "Practice", "//DCIM", "123"));
    }

    @Test
    public void restore() throws Exception {

    }

    @Test
    public void decryptBackup() throws Exception {

    }

    @Test
    public void expire() throws Exception {

    }

}