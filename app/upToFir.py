#!C:\Users\Administrator\AppData\Local\Programs\Python\Python36
# coding=utf-8
# encoding=utf-8
import requests
import sys
import urllib3
urllib3.disable_warnings()
def upToFir():
    # 打印传递过来的参数数组长度，便于校验
    print ('the argLength--->:')
    print (len(sys.argv))
    upUrl = sys.argv[1]
    appName = sys.argv[2]
    bundleId = sys.argv[3]
    verName = sys.argv[4]
    apiToken = sys.argv[5]
    iconPath = sys.argv[6]
    apkPath = sys.argv[7]
    buildNumber = sys.argv[8]
    changeLog = sys.argv[9]
    queryData = {'type': 'android', 'bundle_id': bundleId, 'api_token': apiToken}
    iconDict = {}
    binaryDict = {}
    # 获取上传信息
    try:
        response = requests.post(url=upUrl, data=queryData)
        json = response.json()
        iconDict = (json["cert"]["icon"])
        binaryDict = (json["cert"]["binary"])
    except Exception as e:
        print("query:")
        print(e)


    # 上传logo
    print("上传logo")
    try:
        file = {'file': open(iconPath, 'rb+')}
        param = {"key": iconDict['key'],
                 'token': iconDict['token']}
        req = requests.post(url=iconDict['upload_url'], files=file, data=param, verify=False)
        print ('success_icon:' + req.content)
    except Exception as e:
        print('error_icon:')
        print(e)

    # 上传apk
    print("上传apk")
    try:
        file = {'file': open(apkPath, 'rb')}
        param = {"key": binaryDict['key'],
                 'token': binaryDict['token'],
                 "x:name": appName,
                 "x:version": verName,
                 "x:build": buildNumber,
                 "x:changelog": changeLog}
        req = requests.post(url=binaryDict['upload_url'], files=file, data=param, verify=False)
        print ('success_apk:' + req.content)
    except Exception as e:
        print('error_apk:')
        print(e)


def upToPGY():
    # 蒲公英
    print('蒲公英上传')
    try:
        print("上传apk")
        apk_path = 'E:/python/demo1/app-debug.apk'
        file = {'file': open(apk_path, 'rb')}
        param = {
            "_api_key": '2d50542713698bffb0a4eb8d6ac93608',
            "installType": 1,
            "password": "",
            "updateDescription": '更新日志:修复了某个bug'}
        req = requests.post('https://www.pgyer.com/apiv2/app/upload', files=file, data=param, verify=False)
        print('success:' + req.content)
        json = req.json()
        print('下载地址')
        print(json["data"]["buildQRCodeURL"])
    except Exception as e:
        print('error:', e)
    pass

if __name__ == '__main__':
    print("__main__")
    # upToFir()
    upToPGY()