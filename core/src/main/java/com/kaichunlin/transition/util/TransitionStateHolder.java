package com.kaichunlin.transition.util;

import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Used to help trace the state of transition for debugging purpose
 * <p>
 * Created by Kai-Chun Lin on 2015/5/3.
 */
public class TransitionStateHolder {
    public final String mId;
    private Calendar mStartTime;
    private Calendar mEndTime;
    private Map<String, List<TransitionState>> tranStateMap = new HashMap<>();

    public TransitionStateHolder(String id) {
        mId = id;
    }

    public void start() {
        mStartTime = Calendar.getInstance();
    }

    public void end() {
        mEndTime = Calendar.getInstance();
    }

    public void append(String id, Object appendingClass, String message) {
        append(new TransitionState(id, appendingClass, message));
    }

    public void append(TransitionState transitionState) {
        List<TransitionState> list = tranStateMap.get(transitionState.subId);
        if (list == null) {
            list = new ArrayList<>();
            tranStateMap.put(transitionState.subId, list);
        }
        list.add(transitionState);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("---------- ");
        sb.append(mId);
        sb.append(" Start: ");
        sb.append(mStartTime);
        sb.append(" ----------");

        for (Map.Entry<String, List<TransitionState>> entry : tranStateMap.entrySet()) {
            sb.append("\n");
            sb.append(entry.getValue());
        }
        sb.append("\n--------------------------");
        sb.append(mEndTime);
        sb.append("\n--------------------------");
        return sb.toString();
    }

    public void print() {
        Log.e(getClass().getSimpleName(), "------------- " + mId + " -------------");
        for (Map.Entry<String, List<TransitionState>> entry : tranStateMap.entrySet()) {
            for (TransitionState ts : entry.getValue()) {
                Log.e(getClass().getSimpleName(), ts.toString());
            }
        }
        Log.i(getClass().getSimpleName(), "-----------------------------");
    }

    /**
     * The state of the transition for a given moment
     */
    public static class TransitionState {
        private String subId;
        private Object mClass;
        private String message;
        private long time;

        private TransitionState(String subId, Object appendingClass, String message) {
            this.subId = subId;
            mClass = appendingClass;
            this.message = message;
            time = System.currentTimeMillis();
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("\t<");
            sb.append(subId);
            sb.append(">, t= ");
            sb.append(time);
            sb.append(", ");
            sb.append(mClass.getClass().getSimpleName());
//            sb.append(" [");
//            sb.append(mClass.hashCode());
//            sb.append("]");
            sb.append(":\t");
            sb.append(message);
            return sb.toString();
        }
    }
}
