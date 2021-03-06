/*
 * Copyright (C) 2015  Andrew Gunnerson <andrewgunnerson@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.chenxiaolong.dualbootpatcher.switcher.service;

import android.content.Context;
import android.util.Log;

import com.github.chenxiaolong.dualbootpatcher.socket.MbtoolSocket;
import com.github.chenxiaolong.dualbootpatcher.socket.MbtoolSocket.SwitchRomResult;

import java.io.IOException;

public final class SwitchRomTask extends BaseServiceTask {
    private static final String TAG = SwitchRomTask.class.getSimpleName();

    public final String mRomId;
    private final boolean mForceChecksumsUpdate;
    private final SwitchRomTaskListener mListener;

    public SwitchRomResult mResult;

    public interface SwitchRomTaskListener extends BaseServiceTaskListener {
        void onSwitchedRom(int taskId, String romId, SwitchRomResult result);
    }

    public SwitchRomTask(int taskId, Context context, String romId, boolean forceChecksumsUpdate,
                         SwitchRomTaskListener listener) {
        super(taskId, context);
        mRomId = romId;
        mForceChecksumsUpdate = forceChecksumsUpdate;
        mListener = listener;
    }

    @Override
    public void execute() {
        Log.d(TAG, "Switching to " + mRomId + " (force=" + mForceChecksumsUpdate + ")");

        mResult = SwitchRomResult.FAILED;
        try {
            mResult = MbtoolSocket.getInstance().switchRom(
                    getContext(), mRomId, mForceChecksumsUpdate);
        } catch (IOException e) {
            Log.e(TAG, "mbtool communication error", e);
        }

        mListener.onSwitchedRom(getTaskId(), mRomId, mResult);
    }
}
