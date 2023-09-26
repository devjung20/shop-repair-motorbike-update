import { useCallback, useEffect, useMemo, useState } from "react";
import statisticAPI from "../../api/requests/statistic";
import StatisticPageLayout from "../../layouts/StatisticPageLayout";
import { Card, Space, Table, Typography } from "antd";
import { Link, useLocation } from "react-router-dom";
import { toDDMMYYYY, toYYYY_MM_DD } from "../../utils/converter";
import queryString from "query-string";
import { defaultRenderer, incomeRenderer, moneyRenderer } from "../../utils/renderer";
import TableRowExpand from "../../components/TableRowExpand";
import { Excel } from "antd-table-saveas-excel";
import dayjs from "dayjs";
import TableTop5Product from "../../components/table/TableTop5Product";

const labels = [
  { title: "Linh kiện", dataIndex: "name" },
  { title: "Mã LK", dataIndex: "code" },
  { title: "Đơn vị", dataIndex: "unit" },
  { title: "Số lượng", dataIndex: "quantity" },
  { title: "Giá nhập", dataIndex: "priceIn", render: moneyRenderer },
  { title: "Giá bán", dataIndex: "priceOut", render: moneyRenderer },
  { title: "Lợi nhuận / LK", dataIndex: "monoIncome", render: moneyRenderer },
  { title: "Lợi nhuận", dataIndex: "income", render: moneyRenderer },
];

const ProductStatistic = () => {
  const location = useLocation();
  const params = queryString.parse(location.search);
  const { startTime, endTime } = params;

  const [prodOutIn, setProdOutIn] = useState([]);
  const [prodTop, setProdTop] = useState([]);
  const [loading, setLoading] = useState(false);

  const totalProfit = useMemo(() => prodOutIn.reduce((sum, prod) => (sum += prod.income), 0), [prodOutIn]);

  useEffect(() => {
    setLoading(true);

    const baseParams = {
      startTime: toYYYY_MM_DD(startTime || dayjs().subtract(1, "month")),
      endTime: toYYYY_MM_DD(endTime),
      pageSize: 1000,
      pageNumber: 1,
    };

    Promise.all([statisticAPI.getTopProductStatistic(baseParams), statisticAPI.getProductOutInStatistic(baseParams)])
      .then(([prodTopRes, prodOutInRes]) => {
        setProdTop(prodTopRes?.data?.data?.map((p, i) => ({ ...p, top: i + 1 })));
        setProdOutIn(
          prodOutInRes?.data?.data?.content?.map((p, i) => ({
            ...p,
            id: i + 1,
            quantity: -p.quantity,
            monoIncome: p.priceOut - p.priceIn,
          }))
        );
        setLoading(false);
      })
      .catch((err) => console.log(err));
  }, [endTime, startTime]);

  const exportTable = useCallback(() => {
    new Excel()
      .addSheet("Lợi nhuận từ linh kiện")
      .addColumns(labels)
      .addDataSource(prodOutIn, { str2Percent: true })
      .saveAs(`Thống kê linh kiện - (${toDDMMYYYY(startTime)} - (${toDDMMYYYY(endTime)})).xlsx`);
  }, [prodOutIn, startTime, endTime]);

  const columns = [
    {
      title: "Linh kiện",
      dataIndex: "name",
      render: (name, record) => (
        <Link
          to={`/stock-history?${queryString.stringify({
            productName: record.name,
            unit: record.unit,
            priceIn: record.priceIn,
            priceOut: record.priceOut,
            startTime: toYYYY_MM_DD(startTime || dayjs().subtract(1, "month")),
            endTime: toYYYY_MM_DD(endTime),
          })}`}
        >
          {name}
        </Link>
      ),
    },
    { title: "Mã LK", dataIndex: "code", align: "center", render: defaultRenderer },
    { title: "Đơn vị", dataIndex: "unit", align: "center", render: defaultRenderer },
    { title: "Số lượng", dataIndex: "quantity", align: "center", render: defaultRenderer },
    { title: "Lợi nhuận / linh kiện", dataIndex: "monoIncome", align: "center", render: moneyRenderer },
    { title: "Lợi nhuận", dataIndex: "income", align: "center", render: moneyRenderer },
  ];

  return (
    <StatisticPageLayout
      title="Thống kê linh kiện"
      navigatePath="/statistics/products-income"
      handleExport={exportTable}
    >
      <TableTop5Product products={prodTop} className="mt-8" />
      <Card title="Lợi nhuận từ linh kiện" bordered={false} className="mt-8">
        <Table
          size="small"
          columns={columns}
          rowKey={(record) => record.id}
          dataSource={prodOutIn}
          loading={loading}
          pagination={{ hideOnSinglePage: true, pageSize: 5, size: "default" }}
          expandable={{
            expandedRowRender: (record) => <TableRowExpand record={record} labels={labels} />,
          }}
          scroll={{ x: 576 }}
        />
        <Space className="mt-4 mr-4" style={{ display: "flex", justifyContent: "end" }}>
          <span className="text-semibold">Tổng cộng:</span>
          <span>{incomeRenderer(totalProfit)}</span>
        </Space>
      </Card>
      <Card bordered={false} className="mt-8">
        <Typography.Title level={4} className="mb-0">
          <Space className="mr-4" style={{ display: "flex", justifyContent: "center" }}>
            <span>Lợi nhuận từ linh kiện:</span>
            <span>{incomeRenderer(totalProfit)}</span>
          </Space>
        </Typography.Title>
      </Card>
    </StatisticPageLayout>
  );
};

export default ProductStatistic;
