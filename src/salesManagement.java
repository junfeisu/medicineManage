package src;
import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

class salesIncome extends JPanel {
	public salesIncome() {
		medcineManage.jl_location.setText("  当前位置>>销售管理>>销售收入趋势");
		XYDataset xydataset = createDataset();
		JFreeChart jfreechart = ChartFactory.createTimeSeriesChart("销售收入趋势", "时间", "销售额", xydataset, true, true, true);
		XYPlot xyplot = (XYPlot) jfreechart.getPlot();
		DateAxis dateaxis = (DateAxis) xyplot.getDomainAxis();
		dateaxis.setDateFormatOverride(new SimpleDateFormat("MMM-yyyy"));
		ChartPanel frame1 = new ChartPanel(jfreechart, true);
		dateaxis.setLabelFont(new Font("黑体", Font.BOLD, 14)); // 水平底部标题
		dateaxis.setTickLabelFont(new Font("宋体", Font.BOLD, 12)); // 垂直标题
		ValueAxis rangeAxis = xyplot.getRangeAxis();// 获取柱状
		rangeAxis.setLabelFont(new Font("黑体", Font.BOLD, 15));
		jfreechart.getLegend().setItemFont(new Font("黑体", Font.BOLD, 15));
		jfreechart.getTitle().setFont(new Font("宋体", Font.BOLD, 20));// 设置标题字体
		add(frame1);
	}

	private static XYDataset createDataset() { // 这个数据集有点多，但都不难理解
		TimeSeries timeseries = new TimeSeries("销售收入趋势", org.jfree.data.time.Month.class);
		for(int i = 0; i < 12; i++){
			timeseries.add(new Month(i+1, 2016), Double.valueOf(sqlOperate.getSoleByTime().get(i).get("count")));
		}
		TimeSeriesCollection timeseriescollection = new TimeSeriesCollection();
		timeseriescollection.addSeries(timeseries);
		return timeseriescollection;
	}
}

class salesRank extends JPanel {
	public salesRank() {
		medcineManage.jl_location.setText("  当前位置>>销售管理>>销售排行统计");
		CategoryDataset dataset = getDataSet();
		JFreeChart chart = ChartFactory.createBarChart3D("销售排行", // 图表标题
				"药品类别", // 目录轴的显示标签
				"销售量", // 数值轴的显示标签
				dataset, // 数据集
				PlotOrientation.VERTICAL, // 图表方向：水平、垂直
				true, // 是否显示图例(对于简单的柱状图必须是false)
				false, // 是否生成工具
				false // 是否生成URL链接
		);
		// 从这里开始
		CategoryPlot plot = chart.getCategoryPlot();// 获取图表区域对象
		CategoryAxis domainAxis = plot.getDomainAxis(); // 水平底部列表
		domainAxis.setLabelFont(new Font("黑体", Font.BOLD, 14)); // 水平底部标题
		domainAxis.setTickLabelFont(new Font("宋体", Font.BOLD, 12)); // 垂直标题
		ValueAxis rangeAxis = plot.getRangeAxis();// 获取柱状
		rangeAxis.setLabelFont(new Font("黑体", Font.BOLD, 15));
		chart.getLegend().setItemFont(new Font("黑体", Font.BOLD, 15));
		chart.getTitle().setFont(new Font("宋体", Font.BOLD, 20));// 设置标题字体
		ChartPanel frame1 = new ChartPanel(chart, true);
		add(frame1);
	}

	private static CategoryDataset getDataSet() {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		ArrayList<HashMap<String, String>> al = sqlOperate.getSoleByType();
		if(al.size() < 8){
			for(int i = 0; i < al.size(); i++){			
				dataset.addValue(Integer.parseInt((al.get(i).get("num"))),al.get(i).get("type"),al.get(i).get("type"));
			}
			for(int i =  al.size(); i < 8; i++){			
				dataset.addValue(0,"","");
			}
		}else{
			for(int i =  0; i < 8; i++){			
				dataset.addValue(Integer.parseInt((al.get(i).get("num"))),al.get(i).get("type"),al.get(i).get("type"));
			}
		}
		return dataset;
	}
}
