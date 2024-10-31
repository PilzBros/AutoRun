package com.pilzbros.autorun.io;

public enum Setting {
    CheckForUpdates("CheckForUpdates", true),
    NotifyOnNewUpdates("NotifyOnNewUpdates", true),
    ReportMetrics("MetricReporting", true),
    NotifyOnAustinPilz("NotifyOnPluginCreatorJoin", true);

    private String name;
    private Object def;

    private Setting(String Name, Object Def) {
        this.name = Name;
        this.def = Def;
    }

    public String getString() {
        return this.name;
    }

    public Object getDefault() {
        return this.def;
    }
}
