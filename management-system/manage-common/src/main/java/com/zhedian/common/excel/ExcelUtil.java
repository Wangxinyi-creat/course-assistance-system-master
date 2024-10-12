package com.zhedian.common.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.WriteTable;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhedian.common.domain.CustomExceptionResult;
import com.zhedian.common.domain.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * Excel工具类
 */
@Slf4j
public class ExcelUtil {
    /**
     * 导出数据为excel文件
     *
     * @param filename   文件名称
     * @param dataResult 集合内的bean对象类型要与clazz参数一致
     * @param clazz      集合内的bean对象类型要与clazz参数一致
     * @param response   HttpServlet响应对象
     * @param map        下拉框数据
     */
    public static void export(String filename, List<?> dataResult, Class<?> clazz, HttpServletResponse response, Map map) {
        response.setStatus(200);
        OutputStream outputStream = null;
        ExcelWriter excelWriter = null;
        try {
            if (StringUtils.isBlank(filename)) {
                throw new RuntimeException("'filename' 不能为空");
            }

            //格式处理
            String fileName = filename.concat(".xlsx");
            response.setContentType("application/octet-stream");
            response.setCharacterEncoding("utf8");
            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "utf-8"));
            outputStream = response.getOutputStream();

            //生成ExcelWriter对象
            excelWriter = getTemplateExcelWriter(outputStream, map);

            //创建第一张（sheet）工作表
            WriteSheet writeSheet = EasyExcel.writerSheet(fileName).build();
            //为第一张工作表创建第一张（table）表
            WriteTable writeTable = EasyExcel.writerTable(0).head(clazz).needHead(true).build();

            // 写出数据
            excelWriter.write(dataResult, writeSheet, writeTable);
        } catch (Exception e) {
            log.error("导出excel数据异常：", e);
            throw new CustomExceptionResult(ResultCode.FAILED.getCode(), "导出excel数据失败");
        } finally {
            if (excelWriter != null) {
                excelWriter.finish();
            }
            if (outputStream != null) {
                try {
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    log.error("导出数据关闭流异常", e);
                }
            }
        }
    }

    /**
     * 根据Excel模板，批量导入数据
     *
     * @param file  导入的Excel
     * @param clazz 解析的类型
     * @param <E>   对应的实体类
     */
    public static <E> void importExcel(MultipartFile file, Class<?> clazz, IService<E> service, Class<E> entityClass) {
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("没有文件或者文件内容为空！" );
        }
        List<Object> dataList = null;
        BufferedInputStream ipt = null;
        try {
            InputStream is = file.getInputStream();
            // 用缓冲流对数据流进行包装
            ipt = new BufferedInputStream(is);
            // 数据解析监听器
            ExcelListener<Object, E> listener = new ExcelListener<>(service,entityClass);
            // 读取数据
            EasyExcel.read(ipt, clazz, listener).sheet().doRead();
        } catch (Exception e) {
            log.error(String.valueOf(e));
            throw new RuntimeException("数据导入失败！" + e);
        }
    }


    /**
     * 生成ExcelWriter对象
     *
     * @param outputStream 数据输出流
     * @param map          下拉数据
     * @return 模板下载ExcelWriter对象
     */
    private static ExcelWriter getTemplateExcelWriter(OutputStream outputStream, Map map) {
        return EasyExcel.write(outputStream)
                .registerWriteHandler(new SheetWriteHandler(map))//增加下拉框策略
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())//设置自动适应宽度
                .registerWriteHandler(getStyleStrategy())//字体居中策略
                .build();
    }

    /**
     * 设置表格内容居中显示策略
     *
     * @return
     */
    private static HorizontalCellStyleStrategy getStyleStrategy() {
        // 头的策略
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        //设置表头居中对齐
        headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        // 背景设置为灰色
        headWriteCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        // 头部字体
        WriteFont headWriteFont = new WriteFont();
        headWriteFont.setFontHeightInPoints((short) 10);
        headWriteCellStyle.setWriteFont(headWriteFont);
        // 内容的策略
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        // 设置内容靠中对齐
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        // 内容字体
        WriteFont contentWriteFont = new WriteFont();
        contentWriteFont.setFontHeightInPoints((short) 10);
        contentWriteCellStyle.setWriteFont(contentWriteFont);
        // 执行策略
        HorizontalCellStyleStrategy horizontalCellStyleStrategy = new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);

        return horizontalCellStyleStrategy;
    }

}
