package com.evan.wj.utils;

import com.alibaba.fastjson.JSONObject;
import com.evan.wj.pojo.Book;
import com.evan.wj.pojo.JsonPojo;
import com.sun.corba.se.spi.orbutil.fsm.Input;
import org.apache.commons.compress.utils.IOUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;

public class MyFileUtils {

    /**
     * zip文件压缩
     * @param inputFile 待压缩文件夹/文件名
     * @param outputFile 生成的压缩包名字
     */
    public void ZipCompress(String inputFile, String outputFile) throws Exception {
        //创建zip输出流
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outputFile));
        //创建缓冲输出流
        BufferedOutputStream bos = new BufferedOutputStream(out);
        File input = new File(inputFile);
        compress(out, bos, input,null);
        bos.close();
        out.close();
    }
    /**
     * @param name 压缩文件名，可以写为null保持默认
     */
    //递归压缩
    public static void compress(ZipOutputStream out, BufferedOutputStream bos, File input, String name) throws IOException {
        if (name == null) {
            name = input.getName();
        }
        //如果路径为目录（文件夹）
        if (input.isDirectory()) {
            //取出文件夹中的文件（或子文件夹）
            File[] flist = input.listFiles();

            if (flist.length == 0)//如果文件夹为空，则只需在目的地zip文件中写入一个目录进入
            {
                out.putNextEntry(new ZipEntry(name + "/"));
            } else//如果文件夹不为空，则递归调用compress，文件夹中的每一个文件（或文件夹）进行压缩
            {
                for (int i = 0; i < flist.length; i++) {
                    compress(out, bos, flist[i], name + "/" + flist[i].getName());
                }
            }
        } else//如果不是目录（文件夹），即为文件，则先写入目录进入点，之后将文件写入zip文件中
        {
            out.putNextEntry(new ZipEntry(name));
            FileInputStream fos = new FileInputStream(input);
            BufferedInputStream bis = new BufferedInputStream(fos);
            int len;
            //将源文件写入到zip文件中
            byte[] buf = new byte[1024];
            while ((len = bis.read(buf)) != -1) {
                bos.write(buf,0,len);
            }
            bis.close();
            fos.close();
        }
    }

    /**
     * zip文件解压
     * @param zipFilePath zip文件地址
     * @param destDirPath 解压位置
     * @throws Exception
     */
    public static void zipUncompress(String zipFilePath,String destDirPath) throws Exception {

        File srcFile = new File(zipFilePath);//获取当前压缩文件

        // 判断源文件是否存在
        if (!srcFile.exists()) {
            throw new Exception(srcFile.getPath() + "所指文件不存在");
        }
        ZipFile zipFile = new ZipFile(srcFile);//创建压缩文件对象
        //开始解压
        Enumeration<?> entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            ZipEntry entry = (ZipEntry) entries.nextElement();
            // 如果是文件夹，就创建个文件夹
            if (entry.isDirectory()) {
                String dirPath = destDirPath + "/" + entry.getName();
                srcFile.mkdirs();
            } else {
                // 如果是文件，就先创建一个文件，然后用io流把内容copy过去
                File targetFile = new File(destDirPath + "/" + entry.getName());
                // 保证这个文件的父文件夹必须要存在
                if (!targetFile.getParentFile().exists()) {
                    targetFile.getParentFile().mkdirs();
                }
                targetFile.createNewFile();
                // 将压缩文件内容写入到这个文件中
                InputStream is = zipFile.getInputStream(entry);
                FileOutputStream fos = new FileOutputStream(targetFile);
                int len;
                byte[] buf = new byte[1024];
                while ((len = is.read(buf)) != -1) {
                    fos.write(buf, 0, len);
                }
                // 关流顺序，先打开的后关闭
                fos.close();
                is.close();
            }
        }
    }

    /**
     * 遍历多层结构的文件夹及文件
     */
    public void getAllFile(File dir,boolean dictFlag,ArrayList<String> dictNameList){
        File[] fileArr=dir.listFiles();
        for (File f:fileArr){
            //判断是否是文件夹
            if (f.isDirectory()){
                // 判断是否为根目录
                if(dictFlag){
                    getAllFile(f,false,dictNameList);
                }else {
                    //处理逻辑
                    System.out.println("文件夹"+f.getName());
                    dictNameList.add(f.getName());
                    getAllFile(f,false,dictNameList);
                }
            }else{
                System.out.println("文件"+f.getName());


                StringBuffer sb = new StringBuffer();
                // 处理文件，输出文件
                for (String dictName:dictNameList) {
                    sb.append(dictName);
                }
                System.out.println(sb.toString());

                // 清空文件夹的目录结构list
                dictNameList.clear();

            }
        }
    }

    /**
     * file转multipartFile
     * @throws Exception
     */
    public MultipartFile fileToMultipartFile(File file) throws Exception{
        // File 转 MultipartFile
        FileInputStream input = new FileInputStream(file);
        MultipartFile multipartFile =new MockMultipartFile("file", file.getName(), "text/plain", IOUtils.toByteArray(input));
        return multipartFile;
    }

    /**
     * multipartFile转file
     * @param multipartFile
     * @param path
     * @return
     * @throws Exception
     */
    public File multipartFileToFile(MultipartFile multipartFile,String path) throws Exception{
        // MultipartFile 转 File
        File file = new File(path);
        FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), file);
        // 获取multipartFile的名字
        multipartFile.getOriginalFilename();
        return file;
    }

    /**
     * jar包打包方式读取配置文件
     * @param path properties文件的路径
     */
    public void readJarProperties(String path) {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
        try(Scanner scanner = new Scanner(is)) {
            while (scanner.hasNextLine()) {
                System.out.println(scanner.nextLine());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 用spring的包读取properties文件避免中文乱码
     */
    public void readByUTF8(String path){
        Properties properties = new Properties();
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
        Resource[] resources = new Resource[]{new InputStreamResource(is)};
        try {
            for (Resource resource : resources) {
                PropertiesLoaderUtils.fillProperties(properties, new EncodedResource(resource, "UTF-8"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //这里可以搜索properties的方法和属性
        System.out.println(properties.get("name"));
    }

    /**
     * 读取与处理xml配置文件
     * @param path xml配置文件的位置
     * @throws Exception
     */
    public void readXml(String path) throws Exception {
        //1.创建Reader对象
        SAXReader reader = new SAXReader();

        // 获取xml文件绝对路径
        String absolutePath = this.getClass().getClassLoader().getResource(path).getPath();

        //2.加载xml
        Document document = reader.read(new File(absolutePath));
        //3.获取根节点
        Element rootElement = document.getRootElement();
        Iterator iterator = rootElement.elementIterator();
        while (iterator.hasNext()){
            Element stu = (Element) iterator.next();
            List<Attribute> attributes = stu.attributes();
            System.out.println("======获取属性值======");
            for (Attribute attribute : attributes) {
                System.out.println(attribute.getValue());
            }
            System.out.println("======遍历子节点======");
            Iterator iterator1 = stu.elementIterator();
            while (iterator1.hasNext()){
                Element stuChild = (Element) iterator1.next();
                System.out.println("节点名："+stuChild.getName()+"---节点值："+stuChild.getStringValue());
            }
        }
    }

    /**
     * 获取配置文件的路径
     * @param propertiesPath 配置文件相对路径/配置文件名
     * @return
     */
    public String getAbsolutionPath(String propertiesPath){
        return this.getClass().getClassLoader().getResource(propertiesPath).getPath();
    }

    /**
     * 用alibaba解析json字符串
     * @param jsonStr
     */
    public void readJsonFile(String jsonStr){
        //先转成json对象
        JSONObject jsonObject = JSONObject.parseObject(jsonStr);
        //再获取里面的message对应的值
        String str = jsonObject.getJSONObject("ErrorInfo").get("Message").toString();
        System.out.println(str);

        // 只能获得最外面一层
        System.out.println(jsonObject.containsKey("ErrorInfo"));

        // Javabean对象转换成String类型的JSON字符串
        JSONObject.toJSONString(new JsonPojo());

        // jsonObject转json字符串
        jsonObject.toJSONString();

        //String类型的JSON字符串转换成Javabean对象(有问题)
//        JSONObject.toJavaObject(jsonStr,new JsonPojo());

        // Javabean对象转换成String类型的JSON字符串
        JSONObject.toJSONString(JsonPojo.class);

        // JSON字符串转换成Javabean对象
        JSONObject.parseObject(jsonStr,JsonPojo.class);
    }

    /**
     * 按行读取文件，返回字符串
     * @param fileName
     * @return
     */
    public String readFileByLines(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            StringBuffer stringBuffer = new StringBuffer();
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                System.out.println("line " + line + ": " + tempString);
                line++;
                stringBuffer = stringBuffer.append(tempString);
            }
            reader.close();
            return stringBuffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return null;
    }

    /**
     * 判断文件是否为压缩文件
     * @param file
     * @return
     */
    public boolean isArchiveFile(File file) {

        byte[] ZIP_HEADER_1 = new byte[] { 80, 75, 3, 4 };
        byte[] ZIP_HEADER_2 = new byte[] { 80, 75, 5, 6 };

        if(file == null){
            return false;
        }

        if (file.isDirectory()) {
            return false;
        }

        boolean isArchive = false;
        InputStream input = null;
        try {
            input = new FileInputStream(file);
            byte[] buffer = new byte[4];
            int length = input.read(buffer, 0, 4);
            if (length == 4) {
                isArchive = (Arrays.equals(ZIP_HEADER_1, buffer)) || (Arrays.equals(ZIP_HEADER_2, buffer));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                }
            }
        }
        return isArchive;
    }

    /**
     * 根据正则表达式判断是否匹配
     * @param str
     * @param pattern
     * @return
     */
    public boolean regexPattern(String str,String pattern) {
        Pattern r = Pattern.compile(pattern);
        return r.matcher(str).find();
    }

}
