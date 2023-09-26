import { Line } from "@ant-design/plots";
import { Card } from "antd";
import { moneyRenderer } from "../../utils/renderer";
import { useState } from "react";
import SelectCardHeader from "../SelectCardHeader";
import _ from "lodash";

const ChartInventory12Month = ({ products = [] }) => {
  const [chartSize, setChartSize] = useState(12);

  const config = {
    data: _.flatten(_.chunk(products, 24)?.map((ps) => _.takeRight(ps, chartSize))),
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
      title={<SelectCardHeader title="Biểu đồ xuất nhập kho" value={chartSize} onChange={(val) => setChartSize(val)} />}
    >
      <Line {...config} />
    </Card>
  );
};

export default ChartInventory12Month;
