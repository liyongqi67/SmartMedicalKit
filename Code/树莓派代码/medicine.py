import time

class Medicine (object) :

    def __init__(self,code, name = 'none', type = 'none', indication = 'none', price = 'none', manufacture = 'none', shelf = 'none', remaining = 'none', properties='none', functions='none', usage='none', attentions='none', specifications='none', id = 'none'):
        if isinstance(code,str) & isinstance(name,str)& isinstance(indication,str)& isinstance(type,str)& isinstance(indication,str)& isinstance(price,str)& isinstance(manufacture,str)& isinstance(shelf,str) &isinstance(remaining,str):
            self.name = name
            self.code = code
            self.type = type
            self.indication = indication
            self.price = price
            self.manufacture = manufacture
            self.shelf = shelf
            self.remaining = remaining
            self.properties = properties
            self.functions = functions
            self.specifications = specifications
            self.attentions = attentions
            self.usage = usage
            self.id = id
        else:
            raise ValueError('bad value!please check the enter！')

    def cal_overdue(self):
        if(self.manufacture == 'none'):
            return 9999.0
        ma = list(self.manufacture)
        year = '20' + ma[0] + ma[1]
        month = ma[2] + ma[3]
        day = ma[4] + ma[5]
        month_int = int(month)
        year_int = int(year)
        month_int_line = month_int + int(self.shelf)
        year_int_line = year_int
        if (month_int_line > 12) :
            year_int_line = year_int + month_int_line//12
            month_int_line = month_int_line % 12

        t1 = time.mktime((year_int_line,month_int_line,int(day), 0, 0, 0, 0, 0, 0))
        t_difference = t1 - time.time()

        return t_difference//(24*60*60)
