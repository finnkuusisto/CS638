import random

def genUniqueName():
    firstName = first[int(random.random() * len(first))]
    lastName = last[int(random.random() * len(last))]
    while firstName + ' ' + lastName in taken:
        firstName = first[int(random.random() * len(first))]
        lastName = last[int(random.random() * len(last))]
    taken.add(firstName + ' ' + lastName)
    return firstName,lastName

def printRandomUser(i):
    fName,lName = genUniqueName()
    print '    - !!models.UserInfo'
    print '        username:   ' + fName.lower() + lName.lower()
    print '        email:      ' + 'random.email.' + str(i) + '@gmail.com'
    print '        zipCode:    ' + zips[int(random.random() * len(zips))]
    print '        joinDate:   ' + str(join - int(random.random() * (1000 * 60 * 60 * 24 * 365)))
    print '        fullName:   ' + fName + ' ' + lName
    print '        about:      ' + 'I am an auto-generated user.'
    print '        passHash:   ' + hash
    print '        salt:       ' + salt
    print ''

# most popular names from wikipedia
first = 'James,John,Robert,Michael,William,David,Richard,Charles,Joseph,Thomas,Mary,Patricia,Linda,Barbara,Elizabeth,Jennifer,Maria,Susan,Margaret,Dorothy'.split(',')
last = 'Smith,Johnson,Williams,Brown,Jones,Miller,Davis,Garcia,Rodriguez,Wilson'.split(',')
taken = set()
zips = '37246,53726,40066,51235,63352,68007,73575,78123,87527,95465,98531'.split(',')
join = 1353463497488
hash = 'F3A4749FD47F18996B6579745B14D3E2D791E6F522448AE41F46A1C08CB0D7F2'
salt = '4B813C6F90D2D8C162BB67F16F6E92873258999EC875DEBB5A3571BB875D8322'

for i in range(100):
    printRandomUser(i)
