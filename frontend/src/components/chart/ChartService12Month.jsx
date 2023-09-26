import { Line } from "@ant-design/plots";
import { Card } from "antd";
import { moneyRenderer } from "../../utils/renderer";
import SelectCardHeader from "../SelectCardHeader";
import { useState } from "react";
import _ from "lodash";

const ChartService12Month = ({ services = [] }) => {
  const [chartSize, setChartSize] = useState(12);

  const config = {
    data: _.takeRight(services, chartSize),
    xField: "monthYear",
    yField: "expense",
    seriesField: "name",
    yAxis: {
      label: {
        formatter: (v) =>
          Number(v / 10e5)
            .toFixed(1)
            .toLocaleString() + "M ₫",
      },
    },
    legend: { position: "top" },
    color: ["#5AD8A6", "#FF4D4F", "#5B8FF9"],
    point: {
      size: 5,
      shape: "circle",
      color: ["#5AD8A6", "#FF4D4F", "#5B8FF9"],
    },
    tooltip: {
      showMarkers: true,
      formatter: (datum) => {
        return {
          name: datum.name,
          value: moneyRenderer(datum.expense),
        };
      },
    },
  };

  return (
    <Card
      title={
        <SelectCardHeader
          title="Biểu đồ lợi nhuận từ dịch vụ"
          value={chartSize}
          onChange={(val) => setChartSize(val)}
        />
      }
    >
      <Line {...config} />
    </Card>
  );
};

export default ChartService12Month;
