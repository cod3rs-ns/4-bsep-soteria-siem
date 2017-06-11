package bsep.sw.rule_engine;

import bsep.sw.domain.Log;

import java.util.function.Supplier;

public class FieldSupplier {

    public Supplier<String> getField(final Log log, final FieldType fieldType) {
        switch (fieldType) {
            case MESSAGE:
                return log::getMessage;
            case HOST:
                return log.getInfo()::getHost;
            case LEVEL:
                return log.getLevel()::toString;
            case PID:
                return log.getInfo()::getPid;
            case UID:
                return log.getInfo()::getUid;
            case GID:
                return log.getInfo()::getGid;
            default:
                return null;
        }
    }
}
