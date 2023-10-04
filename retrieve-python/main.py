import os
import shutil

from PyPDF2 import PdfFileReader
from tqdm import tqdm

import 函数


def pdfanalysis(pdfname):
    imgcount = 0
    imgnames = []
    imgpages = []
    imgpaths = []
    title = []
    authors = []
    rootname = ''
    picname = ''
    txtname = ''
    authorsstr = ''
    titlestr = ''
    nextpage = False
    ff = None
    if not os.path.exists(pdfname):
        print('前不见古人，后不见来者，现在连该文件都不见了')
        return ''
    f = open(pdfname, "rb")

    def clean():

        if os.path.exists('input.html'):
            os.remove('input.html')
        if os.path.exists('output.pdf'):
            os.remove('output.pdf')

        #         for root,dirs,files in os.walk(rootname+'/'+titlestr):
        #             for file in files:
        #                 path=os.path.join(root,file)
        #                 os.remove(path)
        if titlestr == '':
            print('先帝创业未半而中道崩殂，还没建立文件夹就挂了')
        else:
            if os.path.exists(rootname + '/' + titlestr):
                shutil.rmtree(rootname + '/' + titlestr)

        os.remove(pdfname)

    try:
        #         imgcount=0
        #         imgnames=[]
        #         imgpages=[]
        #         imgpaths=[]
        #         title=[]
        #         authors=[]
        #         rootname=''
        #         picname=''
        #         txtname=''
        #         authorsstr=''
        #         titlestr=''
        #         f=open(pdfname, "rb")

        if "/" in pdfname:
            namelist = pdfname.split('/')
        elif "\\" in pdfname:
            namelist = pdfname.split('\\')

        pdf = namelist[-1]

        if pdf[-3:] != 'pdf':
            print('你在瞎传什么文件')
            return ''
        temp = namelist[-2]
        name = pdf.replace('.pdf', '')
        rootname = pdfname.replace('/' + temp + '/' + pdf, '').replace('\\' + temp + '\\' + pdf, '')
        #         print(rootname)
        allpages = 函数.get_pdf_page(pdfname)

        pdf_file = PdfFileReader(f, strict=False)

        for page in tqdm(range(allpages)):

            r = 函数.page2pdf(pdf_file, page)
            if not r:
                f.close()
                #                 os.remove(pdfname)
                print('\n文件或许被加密，解密另请高明')
                return ''
            can = 函数.pdf2html()
            if not can:
                print('\n有人相爱，有人夜里看海，有人纯图pdf解析不出来')

            if page == 0 or nextpage:
                if nextpage:
                    nextpage = False
                titlestr, authors, time, keys = 函数.findthings()
                if authors == '0':
                    titlestr = ''
                    authors = []
                    nextpage = True
                    continue

                titles = titlestr

                if len(titlestr) > 50:
                    if ':' in titlestr:
                        titlestr = titlestr.split(':')[0]
                    elif ' ' in titlestr:
                        l = titlestr.split(' ')
                        if len(l) > 50:
                            titlestr = ' '.join(l[:50])
                        else:
                            titlestr = titlestr[:50]
                    else:
                        titlestr = titlestr[:50]

                if os.path.exists(rootname + '/' + titlestr):
                    print('\n书读百遍，其义自见，但相同的论文不用再来一篇')
                    f.close()
                    os.remove(pdfname)
                    return ''

                print('标题：', end='')
                print(titles)

                titlestr = titlestr.replace('/', '或').replace('\\', '或').replace('*', '').replace(' ', '')
                #                 print(page,'***************',titlestr)
                os.mkdir(rootname + '/' + titlestr)

                picname = rootname + '/' + titlestr + '/Picture'
                txtname = rootname + '/' + titlestr + '/' + 'res' + '.txt'

                os.mkdir(picname)
                ff = open(txtname, 'a', encoding='utf-8')

                #         ff.write(name)
                #         ff.write('\n\n')
                ff.write(str(allpages))
                ff.write('\n\n')

                ff.write(titles)
                ff.write('\n\n')

                for i in authors:
                    authorsstr = authorsstr + i

                print('\n作者：', end='')

                if 'ꎻ' in authorsstr:
                    authors = authorsstr.split('ꎻ')

                elif 'ꎬ' in authorsstr:
                    authors = authorsstr.split('ꎬ')
                elif ',' in authorsstr:
                    authors = authorsstr.replace('，', ',').split(',')
                #                     print('作者按逗号分')
                elif '，' in authorsstr:
                    authors = authorsstr.split('，')
                elif '　' in authorsstr:
                    authors = authorsstr.split('　')
                else:
                    authors = authorsstr.split(' ')

                for i in authors:
                    i = i.replace('【', '').replace('】', '')
                    if i != '' and i != '*.' and i != ' ' and i != '  ' and i != '*' and i != '.':
                        print(i, end='')
                        print(';', end='')
                        ff.write(i)
                        ff.write(';')

                print('\n')

                ff.write('\n\n')

                if time != '':
                    print('\n时间：', end='')
                    print(time)
                else:
                    print('\n时间：时间都去哪了~还没找到发表时间就老了~')

                if keys != '':
                    print('\n标签：', end='')
                    print(keys)
                else:
                    print('\n标签：关键问题关键在于关键词找不到，真的很关键')

                if time != '':
                    ff.write(time)
                else:
                    ff.write('null')

                ff.write('\n\n')

                if keys != '':
                    ff.write(keys)
                else:
                    ff.write('null')

                ff.write('\n\n')

            if not nextpage:
                ff.write('页码' + str(page + 1))
                ff.write('\n')

                imgs = 函数.html2txt(ff, titles, authorsstr)

                imgcount, count = 函数.pdf2img(picname, imgcount)
                imgpages = imgpages + [page + 1] * count

        #             if len(imgs)!=count:
        #                 imgs=['null']*count
        #             imgnames=imgnames+imgs

        ff.write('\n-1\n\n\n')

        imgpaths = os.listdir(picname)
        if len(imgpaths) > 99:
            print('有没有搞错啊，那么多张图啊，合理吗，不识别了自己玩去')
            ff.write('\n0\n\n\n')
        else:
            函数.img2txt(imgpages, imgpaths, ff, picname)

        ff.close()

        shutil.copyfile(pdfname, rootname + '/' + titlestr + '/' + titlestr + '.pdf')
        print("----------------------")
        print(rootname + '/' + titlestr + '/' + titlestr + '.pdf')

        os.remove('input.html')
        os.remove('output.pdf')

        f.close()
        os.remove(pdfname)
        return titlestr

    except Exception as e:

        print(e)
        f.close()
        clean()
        return ''