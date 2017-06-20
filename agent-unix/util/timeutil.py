from datetime import datetime
import pytz

formats = [
    '%Y-%m-%d %H:%M:%S,%f',
    '%Y-%m-%d %H:%M:%S.%f',
    '%b %d  %H:%M:%S',
    '%m/%d/%y %H:%M:%S:%f'
]


def time_iso_8601(time, timezone='UTC'):
    tz = pytz.timezone(timezone)
    for time_format in formats:
        try:
            parsed_time = datetime.strptime(time, time_format)
            if parsed_time is not None:
                return tz.localize(parsed_time).isoformat()
        except ValueError:
            pass

    return tz.localize(datetime.today()).isoformat()
