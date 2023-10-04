import os
import re

import fitz
import pdfplumber
from PyPDF2 import PdfFileWriter
from bs4 import BeautifulSoup
from pdfminer.pdfparser import PDFSyntaxError

import ocr


def findthings():
    fff = open('input.html', 'rb')
    t = fff.read()
    bs = BeautifulSoup(t, "html.parser")
    txt = bs.text.replace('【', '\n【').replace(' ', '')
    bs = str(bs)
    fontsizes = re.findall(r'font-size:(.*?)pt', bs)

    for i in range(len(fontsizes)):
        fontsizes[i] = eval(fontsizes[i])
    fontsizes = fontsizes[:int(len(fontsizes) / 1.2)]
    #     print(fontsizes)

    sizes = list(set(fontsizes))
    #     print(bs)

    title = ''
    titlesize = str(max(sizes))

    temp = re.findall(r'font-size:' + titlesize + '.*?>(.*?)<', bs)

    tttt = ''

    for i in temp:
        tttt = tttt + i
    #     print('******'+tttt+'******')
    tttt = tttt.replace(' ', '')
    while '论著' in tttt or tttt == '' or len(tttt) < 3:
        sizes.remove(eval(titlesize))
        titlesize = str(max(sizes))
        temp = re.findall(r'font-size:' + titlesize + '.*?>(.*?)<', bs)

        tttt = ''

        for i in temp:
            tttt = tttt + i
        tttt = tttt.replace(' ', '')
        print('******' + titlesize + '*****' + tttt + '******')

    #     print(titlesize)
    findtitle = False
    findauthor = False

    authors = []
    authorsize = 0
    www = ''
    lll = ''
    b = bs.split('\n')
    #     print(titlesize)
    for i in b:

        #         print(i)
        w = re.findall(r'top:(.*?)pt;', i)
        n = re.findall(r'font-size:(.*?)pt', i)
        l = re.findall(r'line-height:(.*?)pt', i)
        if n != [] and w != [] and l != []:
            nn = n[0]
            ww = w[0]
            ll = l[0]
            #             print(nn)
            if titlesize in n:
                #                 print('找到标题')
                titles = re.findall(r'<.*?font-family.*?>(.*?)</span>', i)
                for j in titles:
                    title = title + j
                findtitle = True
            elif findtitle and titlesize not in n and not findauthor:
                #                 print('找到作者')
                #                 authorsize=nn
                #                 print(authorsize)
                www = ww
                lll = ll
                author = re.findall(r'<.*?font-family.*?>(.*?)</span>', i)
                for j in author:
                    #                     if '\u3000'not in j:
                    j = j.replace('0', '').replace('1', '').replace('2', '').replace('3', '').replace('4', '').replace(
                        '5', '').replace('6', '').replace('7', '').replace('8', '').replace('9', '').replace('(',
                                                                                                             '').replace(
                        ')', '').replace('（', '').replace('）', '').replace('１,３', '')
                    authors.append(j)
                authors.append(' ')

                findauthor = True
            #             elif findauthor and nn==authorsize:
            elif www != '' and abs(eval(www) - eval(ww)) <= 2 and lll in l:
                author = re.findall(r'<.*?font-family.*?>(.*?)</span>', i)
                for j in author:
                    #                     if '\u3000'not in j:
                    authors.append(j)
                authors.append(' ')
            elif www != '' and authors == [] and abs(eval(www) - eval(ww)) <= 2:
                author = re.findall(r'<.*?font-family.*?>(.*?)</span>', i)
                for j in author:
                    #                     if '\u3000'not in j:
                    authors.append(j)
                    authors.append(' ')
    #             elif findauthor and nn!=authorsize:
    # #                 print('作者结束')
    #                 break
    if '首发论文' in title:
        print('第一页是封面，换下一页')
        return title, '0', '0', '0'

    #     print("寻找发表时间:")

    time = re.findall(r'(\d+年\d+月)第', txt)
    if time == []:
        time = re.findall(r'(\d+年\d+月)', txt)
        if time == []:
            time = ''
        else:
            time = time[0]
            number = re.findall(r'\d+', time)
            if len(number[1]) < 2:
                time = str(int(number[0])) + '-0' + str(int(number[1])) + '-01'
            elif int(number[1]) < 100:
                time = str(int(number[0])) + '-' + str(int(number[1])) + '-01'
            else:
                time = ''
            if int(number[0]) > 9999 or int(number[0]) < 1000:
                time = ''
    #         print('发表时间解析错误')
    else:
        time = time[0]
        number = re.findall(r'\d+', time)
        if len(number[1]) < 2:
            time = str(int(number[0])) + '-0' + str(int(number[1])) + '-01'
        elif int(number[1]) < 100:
            time = str(int(number[0])) + '-' + str(int(number[1])) + '-01'
        else:
            time = ''
        if int(number[0]) > 9999 or int(number[0]) < 1000:
            time = ''
    #     print(time)

    #     authors=sorted(list(set(authors)),key=authors.index)
    findCkey = False
    findFC = False
    Ckeytype = ''
    Csamet = False

    findEkey = False
    findFE = False
    Ekeytype = ''
    Esamet = False
    lll = ''
    keys = ''
    #     for i in b:
    #         print(i)
    for i in b:

        wtype = re.findall(r'top:(.*?)pt;', i)
        l = re.findall(r'line-height:(.*?)pt', i)
        if wtype != []:
            wtypep = wtype[0]
            ll = l[0]
            ii = i.replace(' ', '')
            if '关键词' in ii or '键词' in ii:
                #                     print('找到前词')
                Ckeytype = wtypep
                lll = ll
                findCkey = True
                key = re.findall(r'font-family:.*?>(.*?)</span>', i)

                for j in key:
                    j = j.replace(' ', '')
                    #                             if 'Keyword' not in j and '【'not in j and '】'not in j:
                    keys = keys + j
                #                         Csamet=True
                keys = keys.replace('关键词', '').replace('Keywords', '').replace(':', '').replace('：', '').replace(
                    'Keyword', '').replace('［', '').replace('］', '')
            #                     print("\n\n\n-*"+keys)
            elif Ckeytype != '' and abs(eval(Ckeytype) - eval(wtypep)) <= 2 and lll in l:
                #                 elif findCkey and not findFC and not Csamet:
                # #                     print('找到第一个关键词')

                #                     Ckeytype=wtype
                #         #             print(Ckeytype)
                #         #             print(i)
                key = re.findall(r'font-family:.*?>(.*?)</span>', i)

                for j in key:
                    keys = keys + j
            #                     print("-**"+keys)
            elif Ckeytype != '' and keys == '' and abs(eval(Ckeytype) - eval(wtypep)) <= 2:
                key = re.findall(r'font-family:.*?>(.*?)</span>', i)

                for j in key:
                    keys = keys + j
    #                     print("-***"+keys)
    #                     findFC=True
    #                 elif findFC:
    # #                     print('继续寻找关键词')
    #     #                 print(wtype)
    #                     if wtype!=Ckeytype:
    #                         break
    #                     key=re.findall(r'<.*?font-family:.*?>(.*?)</span>',i)
    #                     for j in key:
    #                         keys=keys+j
    if keys != '':
        keys = keys + ';'

    lll = ''
    for i in b:

        wtype = re.findall(r'top:(.*?)pt;', i)
        l = re.findall(r'line-height:(.*?)pt', i)
        if wtype != []:
            wtypep = wtype[0]
            ll = l[0]
            ii = i.replace(' ', '')
            if 'Keyword' in ii or 'KeyWord' in ii:
                #                     print('找到前词')
                Ekeytype = wtypep
                lll = ll
                findEkey = True
                key = re.findall(r'font-family:.*?>(.*?)</span>', i)
                #                     if len(key)>3:
                for j in key:
                    j = j.replace(' ', '')
                    #                             if 'Keyword' not in j and '【'not in j and '】'not in j:
                    keys = keys + j
            #                         Esamet=True
            elif Ekeytype != '' and abs(eval(Ekeytype) - eval(wtypep)) <= 2 and lll in l:
                #                 elif findEkey and not findFE and not Esamet:
                # #                     print('找到第一个关键词')

                #                     Ekeytype=wtype
                #         #             print(i)
                key = re.findall(r'font-family:.*?>(.*?)</span>', i)

                for j in key:
                    keys = keys + j
            elif Ekeytype != '' and keys == '' and abs(eval(Ekeytype) - eval(wtypep)) <= 2:
                key = re.findall(r'font-family:.*?>(.*?)</span>', i)

                for j in key:
                    keys = keys + j
    #                     findFE=True
    #                 elif findFE:
    # #                     print('继续寻找关键词')
    #     #                 print(wtype)
    #                     if wtype!=Ekeytype:
    #                         break
    #                     key=re.findall(r'<.*?font-family:.*?>(.*?)</span>',i)
    #                     for j in key:
    #                         keys=keys+j
    keys = keys.replace('；', ';').replace('[', '').replace(']', '').replace('【', '').replace('】', '').replace('关键词',
                                                                                                              '').replace(
        'Keywords', '').replace(':', '').replace('：', '').replace('Keyword', '').replace('［', '').replace('］',
                                                                                                          '').replace(
        ',', ';').replace('，', ';')

    if 'ꎻ' in keys:
        keys = keys.replace('ꎻ', ';')

    elif 'ꎬ' in keys:
        keys = keys.replace('ꎬ', ';')
    elif ';' in keys:
        keys = keys.replace(';;', ';')
    elif ' ' in keys:
        keys.replace(' ', ';').replace('；', ';')

    fff.close()

    return title, authors, time, keys


def page2pdf(pdf_file, i):
    output = PdfFileWriter()

    try:
        output.addPage(pdf_file.getPage(i))
        outputStream = open("output.pdf", "wb")
        output.write(outputStream)
        return 1
    except:
        return 0


def get_pdf_page(pdf_path):
    try:
        f = pdfplumber.open(pdf_path)
        page = len(f.pages)
    except PDFSyntaxError:
        page = 0
    return page


def pdf2html():
    doc = fitz.open("output.pdf")
    for page in doc:
        html_content = page.get_text('html')
        with open("input.html", 'w', encoding='utf8', newline="") as fp:
            fp.write(html_content)
    f = open('input.html', 'rb')
    t = f.read()
    bs = BeautifulSoup(t, "html.parser")
    bs = str(bs)
    if bs == '':
        return 0
    else:
        return 1
    f.close()


def html2txt(ff, titlestr, authorsstr):
    f = open('input.html', 'rb')
    t = f.read()
    bs = BeautifulSoup(t, "html.parser")
    txt = bs.text.replace('【', '\n【')
    txt = txt.replace(titlestr, '')
    txt = txt.replace(authorsstr, '')
    txt = txt.split('\n')

    imgs = []
    title = False
    findimg = False

    for i in txt:

        #                 if re.match('图 \d+',i) or re.match('Fig. \d+.',i):
        #                     findimg=True
        #                     continue
        #                 if findimg:
        #                     imgname=i.replace('，','。').replace(',','。').split('。')[0]
        #                     imgs.append(imgname)
        #                     findimg=False
        if i != '' and not (i[-1] == "。" or i[-1] == "."):
            #                     print(i,end='')
            ff.write(i)
        else:
            ff.write(i)
            ff.write('\n\n')
    #                     print(i)

    f.close()
    return imgs


def img2txt(imgpages, imgpaths, ff, picname):
    tp = 0
    count = 0
    for i in range(len(imgpages)):
        if tp != imgpages[i]:
            tp = imgpages[i]
            st = '页码' + str(tp)
            ff.write(st)
            ff.write('\n\n')
            ff.write(imgpaths[i].replace('.png', ''))
            ff.write('\n')
            reader = ocr.Reader(['ch_sim', 'en'], gpu=True)
            txts = reader.readtext(picname + '/' + imgpaths[i], detail=0)
            txt = ''
            for t in txts:
                txt = txt + t + ' '
            if txt == '':
                ff.write('null')
            else:
                ff.write(txt)

            ff.write('\n\n')
            count += 1
        else:
            ff.write(imgpaths[i].replace('.png', ''))
            ff.write('\n')
            reader = ocr.Reader(['ch_sim', 'en'], gpu=True)
            txts = reader.readtext(picname + '/' + imgpaths[i], detail=0)
            txt = ''
            for t in txts:
                txt = txt + t + ' '
            if txt == '':
                ff.write('null')
            else:
                ff.write(txt)

            ff.write('\n\n')
            count += 1

    ff.write('\n0')


def pdf2img(save_path, imgcount):
    count = 0
    checkXO = r"/Type(?= */XObject)"
    checkIM = r"/Subtype(?= */Image)"

    doc = fitz.open('output.pdf')

    lenXREF = doc.xref_length()

    for i in range(1, lenXREF):
        try:

            text = doc.xref_object(i)
            isXObject = re.search(checkXO, text)

            isImage = re.search(checkIM, text)

            if not isXObject or not isImage:
                continue

            pix = fitz.Pixmap(doc, i)

            if imgcount >= 10:
                new_name = "img{}.png".format(imgcount)
            else:
                new_name = 'img0' + str(imgcount) + '.png'

            new_name = new_name.replace(':', '')

            if pix.n < 5:
                pix.save(os.path.join(save_path, new_name))
            else:
                pix0 = fitz.Pixmap(fitz.csRGB, pix)
                pix0.save(os.path.join(save_path, new_name))
                pix0 = None
            pix = None
            imgcount += 1
            count += 1
        except:
            continue

    return imgcount, count
