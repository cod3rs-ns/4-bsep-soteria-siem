package bsep.sw.rule_engine;

import bsep.sw.domain.Log;
import bsep.sw.domain.LogError;

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
            case SOURCE:
                return log.getInfo()::getSource;
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

    public Supplier<String> getErrorField(final Log log, final FieldType fieldType, final LogError errorError) {
        switch (fieldType) {
            case ERROR_TYPE:
                return errorError::getType;
            case ERROR:
                return errorError::getError;
            case ERROR_NO:
                return errorError::getErrno;
            case STACK:
                return errorError::getStack;
            default:
                return null;
        }
    }
}
