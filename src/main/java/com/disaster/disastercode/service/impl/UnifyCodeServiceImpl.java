package com.disaster.disastercode.service.impl;

import com.alibaba.excel.EasyExcel;
import com.disaster.disastercode.VO.*;
import com.disaster.disastercode.common.ErrorCode;
import com.disaster.disastercode.exception.BusinessException;
import com.disaster.disastercode.model.domain.DetailDisaster;
import com.disaster.disastercode.service.DetailDisasterService;
import com.disaster.disastercode.service.UnifyCodeService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UnifyCodeServiceImpl implements UnifyCodeService {
    private List<LocationLine> locationLineList;
    private List<DisasterInfoLine> disasterInfoLineList;
    private List<DisasterPointLine> disasterPointLineList;
    private Hashtable<String, String> sourceSubMap1;
    private Hashtable<String, String> sourceSubMap2;

    @Resource
    private DetailDisasterService detailDisasterService;

    public UnifyCodeServiceImpl() {

        try {
            this.initInfoReadFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // 来源码子类Map
        this.sourceSubMap1 = new Hashtable<>();
        sourceSubMap1.put("00", "前方地震应急指挥部");
        sourceSubMap1.put("01", "后方地震应急指挥部");
        sourceSubMap1.put("20", "应急指挥技术系统");
        sourceSubMap1.put("21", "社会服务工程应急救援系统");
        sourceSubMap1.put("40", "危险区预评估工作组");
        sourceSubMap1.put("41", "地震应急指挥技术协调组");
        sourceSubMap1.put("42", "震后政府信息支持工作项目组");
        sourceSubMap1.put("80", "灾情快速上报接收处理系统");
        sourceSubMap1.put("81", "地方地震局应急信息服务相关技术系统");
        sourceSubMap1.put("99", "其他");

        this.sourceSubMap2 = new Hashtable<>();
        sourceSubMap2.put("00", "互联网感知");
        sourceSubMap2.put("01", "通信网感知");
        sourceSubMap2.put("02", "舆情网感知");
        sourceSubMap2.put("03", "电力系统感知");
        sourceSubMap2.put("04", "交通系统感知");
        sourceSubMap2.put("05", "其他");
    }

    private void initInfoReadFile() throws IOException {
        {
            // 读取本地文件获取行政区划表
            ClassPathResource classPathResource = new ClassPathResource("area-number-2020.txt");
            if (!classPathResource.exists())
                throw new RuntimeException("行政区划文件不存在");

            InputStreamReader inputStreamReader = new InputStreamReader(classPathResource.getInputStream(), StandardCharsets.UTF_8);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String lineText;
            ArrayList<LocationLine> locationLines = new ArrayList<>();

            while ((lineText = bufferedReader.readLine()) != null) {
                String[] splits = lineText.split("\t");
                LocationLine locationLine = new LocationLine();
                locationLine.setLevel(Integer.parseInt(splits[0]));
                locationLine.setLocationCode(splits[1]);
                locationLine.setLocationName(splits[2]);

                locationLines.add(locationLine);
            }
            this.locationLineList = locationLines;
            inputStreamReader.close();
        }

        {
            // 读取本地文件获取灾情信息
            ClassPathResource classPathResource = new ClassPathResource("disasterInfo.txt");
            if (!classPathResource.exists())
                throw new RuntimeException("灾情信息文件不存在");

            InputStreamReader inputStreamReader = new InputStreamReader(classPathResource.getInputStream(), StandardCharsets.UTF_8);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String lineText;
            ArrayList<DisasterInfoLine> disasterInfoLines = new ArrayList<>();

            while ((lineText = bufferedReader.readLine()) != null) {
                String[] splits = lineText.split(" ");
                DisasterInfoLine disasterInfoLine = new DisasterInfoLine();
                disasterInfoLine.setDisasterMainCode(splits[0]);
                disasterInfoLine.setDisasterMain(splits[1]);
                disasterInfoLine.setDisasterSubCode(splits[2]);
                disasterInfoLine.setDisasterSub(splits[3]);

                disasterInfoLines.add(disasterInfoLine);
            }
            this.disasterInfoLineList = disasterInfoLines;
            inputStreamReader.close();
        }

        {
            // 读取本地文件获取灾情指标
            ClassPathResource classPathResource = new ClassPathResource("disasterPoint.txt");
            if (!classPathResource.exists())
                throw new RuntimeException("灾情指标文件不存在");

            InputStreamReader inputStreamReader = new InputStreamReader(classPathResource.getInputStream(), StandardCharsets.UTF_8);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String lineText;
            ArrayList<DisasterPointLine> disasterPointLines = new ArrayList<>();

            while ((lineText = bufferedReader.readLine()) != null) {
                String[] splits = lineText.split(" ");
                DisasterPointLine disasterPointLine = new DisasterPointLine();
                disasterPointLine.setDisasterTypeCode(splits[0]);
                disasterPointLine.setDisasterType(splits[1]);
                disasterPointLine.setDisasterPointCode(splits[2]);
                disasterPointLine.setDisasterPoint(splits[3]);

                disasterPointLines.add(disasterPointLine);
            }
            this.disasterPointLineList = disasterPointLines;
            inputStreamReader.close();
        }
    }

    @Override
    public DetailDisasterForm getInfoFromCode(UnifyCode unifyCode) {
        DetailDisasterForm detailDisasterForm = new DetailDisasterForm();

        // 12位 地理灾情码
        detailDisasterForm.setLocation(fromLocationCode(unifyCode));
        // 14位 时间码
        String timeCode = unifyCode.getCode().substring(12, 26);
        String year = timeCode.substring(0, 4);
        String month = timeCode.substring(4, 6);
        String day = timeCode.substring(6, 8);
        String hour = timeCode.substring(8, 10);
        String minus = timeCode.substring(10, 12);
        String second = timeCode.substring(12, 14);

        String occurTime = year + "-" + month + "-" + day + " " + hour + ":" + minus + ":" + second;
        detailDisasterForm.setOccurTime(occurTime);
        // 3位 来源码
        fromSourceCode(unifyCode, detailDisasterForm);
        // 1位 载体码
        int codeType = unifyCode.getCodeType();
        switch (codeType) {
            case 0:
                detailDisasterForm.setCodeType("文字");
                break;
            case 1:
                detailDisasterForm.setCodeType("图像");
                break;
            case 2:
                detailDisasterForm.setCodeType("音频");
                break;
            case 3:
                detailDisasterForm.setCodeType("视频");
                break;
            case 4:
                detailDisasterForm.setCodeType("其他");
                break;
        }

        // 灾情码 6位
        fromDisasterInfo(unifyCode, detailDisasterForm);
        detailDisasterForm.setDescription(unifyCode.getDescription());
        return detailDisasterForm;
    }

    @Override
    public Boolean insertCodeByExcel(MultipartFile excel) {
        List<UnifyCode> unifyCodes;
        try {
            unifyCodes = EasyExcel.read(excel.getInputStream())
                    .head(UnifyCode.class).sheet().doReadSync();
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "读取excel文件错误!");
        }
        List<DetailDisaster> detailDisasterList = new ArrayList<>();
        for (UnifyCode unifyCode : unifyCodes) {
            detailDisasterList.add(this.getInfoFromCode(unifyCode).formDetailDisaster());
        }
        boolean rst = detailDisasterService.saveBatch(detailDisasterList);
        if (!rst) {
            throw new BusinessException(ErrorCode.INNER_ERROR, "插入excel文字灾情数据失败");
        }
        return true;
    }

    private Location fromLocationCode(UnifyCode unifyCode) {
        String locationStr = unifyCode.getCode().substring(0, 12);
        Location location = new Location();
        // 1级 省 2位
        String s1 = locationStr.substring(0, 2);
        List<LocationLine> list1 = locationLineList.stream().filter(
                        line -> line.getLevel() == 1 && line.getLocationCode().substring(0, 2).equals(s1))
                .collect(Collectors.toList());
        if (list1.size() > 0) {
            String locationName = list1.get(0).getLocationName();
            location.setProvince(locationName);
        } else {
            location.setProvince("");
        }
        // 2级 市 2位
        String s2 = locationStr.substring(0, 4);
        List<LocationLine> list2 = locationLineList.stream().filter(
                        line -> line.getLevel() == 2 && line.getLocationCode().substring(0, 4).equals(s2))
                .collect(Collectors.toList());
        if (list2.size() > 0) {
            String locationName = list2.get(0).getLocationName();
            location.setCity(locationName);
        } else {
            location.setCity("");
        }
        // 3级 县 2位
        String s3 = locationStr.substring(0, 6);
        List<LocationLine> list3 = locationLineList.stream().filter(
                        line -> line.getLevel() == 3 && line.getLocationCode().substring(0, 6).equals(s3))
                .collect(Collectors.toList());
        if (list3.size() > 0) {
            String locationName = list3.get(0).getLocationName();
            location.setCounty(locationName);
        } else {
            location.setCounty("");
        }
        // 4级 镇 3位
        String s4 = locationStr.substring(0, 9);
        List<LocationLine> list4 = locationLineList.stream().filter(
                        line -> line.getLevel() == 4 && line.getLocationCode().substring(0, 9).equals(s4))
                .collect(Collectors.toList());
        if (list4.size() > 0) {
            String locationName = list4.get(0).getLocationName();
            location.setTown(locationName);
        } else {
            location.setTown("");
        }
        // 5级 村 3位
        String s5 = locationStr.substring(0, 12);
        List<LocationLine> list5 = locationLineList.stream().filter(
                        line -> line.getLevel() == 5 && line.getLocationCode().substring(0, 12).equals(s5))
                .collect(Collectors.toList());
        if (list5.size() > 0) {
            String locationName = list5.get(0).getLocationName();
            location.setVillage(locationName);
        } else {
            location.setVillage("");
        }
        return location;
    }

    private void fromSourceCode(UnifyCode unifyCode, DetailDisasterForm detailDisasterForm) {
        String sourceMainCode = unifyCode.getCode().substring(26, 27);
        String sourceSubCode = unifyCode.getCode().substring(27, 29);

        String sourceMain = "";
        String sourceSub = "";

        switch (sourceMainCode) {
            case "1":
                sourceMain = "业务报送数据";
                break;
            case "2":
                sourceMain = "泛在感知数据";
                break;
            case "3":
                sourceMain = "其他数据";
                break;
        }

        if (sourceMainCode.equals("1")) {
            sourceSub = this.sourceSubMap1.get(sourceSubCode);
        }

        if (sourceMainCode.equals("2")) {
            sourceSub = this.sourceSubMap2.get(sourceSubCode);
        }

        detailDisasterForm.setSourceMain(sourceMain);
        detailDisasterForm.setSourceSub(sourceSub);
    }

    private void fromDisasterInfo(UnifyCode unifyCode, DetailDisasterForm detailDisasterForm) {
        String code = unifyCode.getCode();
        String disasterMainCode = code.substring(30, 31);
        String disasterSubCode = code.substring(31, 33);
        String disasterPointCode = code.substring(33, 36);

        List<DisasterInfoLine> list1 = this.disasterInfoLineList.stream().filter(
                line -> line.getDisasterMainCode().equals(disasterMainCode) &&
                        line.getDisasterSubCode().equals(disasterSubCode)
        ).collect(Collectors.toList());

        if (list1.size() > 0) {
            DisasterInfoLine disasterInfoLine = list1.get(0);
            detailDisasterForm.setDisasterMain(disasterInfoLine.getDisasterMain());
            detailDisasterForm.setDisasterSub(disasterInfoLine.getDisasterSub());
        } else {
            detailDisasterForm.setDisasterMain("");
            detailDisasterForm.setDisasterSub("");
        }

        List<DisasterPointLine> list2 = this.disasterPointLineList.stream().filter(
                line -> line.getDisasterTypeCode().equals(disasterMainCode) &&
                        line.getDisasterPointCode().equals(disasterPointCode)
        ).collect(Collectors.toList());

        if (list2.size() > 0) {
            DisasterPointLine disasterInfoLine = list2.get(0);
            detailDisasterForm.setDisasterType(disasterInfoLine.getDisasterType());
            detailDisasterForm.setDisasterPoint(disasterInfoLine.getDisasterPoint());
        } else {
            detailDisasterForm.setDisasterType("");
            detailDisasterForm.setDisasterPoint("");
        }
    }
}
