class Schedule (object):

    def __init__(self, hour, minute, last, name, dosage):
        self.name = name
        self.hour = hour
        self.minute = minute
        self.last = last
        self.dosage = dosage

    def last_time(self):
        minute = int(self.minute)
        hour = int(self.hour)
        minute += int(self.last)
        while minute > 59:
            minute -= 60
            hour += 1
        return hour, minute

