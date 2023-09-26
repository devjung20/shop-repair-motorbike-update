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

const baseLabels = [
  { title: "Linh kiện", dataIndex: "name" },
  { title: "Mã linh kiện", dataIndex: "code" },
  { title: "Đơn vị", dataIndex: "unit" },
  { title: "Giá tiền", dataIndex: "price", render: moneyRenderer },
  { title: "Phân loại", dataIndex: "income", render: (income) => (income > 0 ? "Xuất hàng" : "Nhập hàng") },
  { title: "Số lượng", dataIndex: "quantity" },
];

const inLabels = [...baseLabels, { title: "Chi phí", dataIndex: "income", render: moneyRenderer }];

const outLabels = [...baseLabels, { title: "Doanh thu", dataIndex: "income", render: moneyRenderer }];

const InventoryStatistic = () => {
  const location = useLocation();
  const params = queryString.parse(location.search);
  const { startTime, endTime } = params;

  const [prodIn, setProdIn] = useState([]);
  const [prodOut, setProdOut] = useState([]);
  const [loading, setLoading] = useState(false);

  const totalLost = useMemo(() => prodIn.reduce((sum, prod) => (sum += prod.income), 0), [prodIn]);
  const totalEarn = useMemo(() => prodOut.reduce((sum, prod) => (sum += prod.income), 0), [prodOut]);

  useEffect(() => {
    setLoading(true);

    const baseParams = {
      startTime: toYYYY_MM_DD(startTime || dayjs().subtract(1, "month")),
      endTime: toYYYY_MM_DD(endTime),
      pageSize: 1000,
      pageNumber: 1,
    };

    Promise.all([statisticAPI.getProductInStatistic(baseParams), statisticAPI.getProductOutStatistic(baseParams)])
      .then(([prodInRes, prodOutRes]) => {
        setProdIn(prodInRes?.data?.data?.content?.map((p, i) => ({ ...p, id: i + 1 })));
        setProdOut(prodOutRes?.data?.data?.content?.map((p, i) => ({ ...p, id: i + 1 })));
        setLoading(false);
      })
      .catch((err) => console.log(err));
  }, [endTime, startTime]);

  const exportTable = useCallback(() => {
    new Excel()
      .addSheet("Thống kê nhập linh kiện")
      .addColumns(inLabels)
      .addDataSource(prodIn, { str2Percent: true })
      .addSheet("Thống kê xuất linh kiện")
      .addColumns(outLabels)
      .addDataSource(prodOut, { str2Percent: true })
      .saveAs(`Thống kê kho - (${toDDMMYYYY(startTime)} - (${toDDMMYYYY(endTime)})).xlsx`);
  }, [prodIn, prodOut, startTime, endTime]);

  const columnsOut = [
    {
      title: "Linh kiện",
      dataIndex: "name",
      render: (name, record) => (
        <Link
          to={`/stock-history?${queryString.stringify({
            productName: record.name,
            unit: record.unit,
            priceOut: record.price,
            action: "EXPORT",
            startTime: toYYYY_MM_DD(startTime || dayjs().subtract(1, "month")),
            endTime: toYYYY_MM_DD(endTime),
          })}`}
        >
          {name}
        </Link>
      ),
    },
    { title: "Mã linh kiện", dataIndex: "code", align: "center", render: defaultRenderer },
    { title: "Đơn vị", dataIndex: "unit", align: "center", render: defaultRenderer },
    { title: "Số lượng", dataIndex: "quantity", align: "center", render: defaultRenderer },
    { title: "Giá tiền", dataIndex: "price", align: "center", render: moneyRenderer },
    { title: "Doanh thu", dataIndex: "income", align: "center", render: moneyRenderer },
  ];

  const columnsIn = [
    {
      title: "Linh kiện",
      dataIndex: "name",
      render: (name, record) => (
        <Link
          to={`/stock-history?${queryString.stringify({
            productName: record.name,
            unit: record.unit,
            priceIn: record.price,
            action: "IMPORT",
            startTime: toYYYY_MM_DD(startTime || dayjs().subtract(1, "month")),
            endTime: toYYYY_MM_DD(endTime),
          })}`}
        >
          {name}
        </Link>
      ),
    },
    { title: "Mã linh kiện", dataIndex: "code", align: "center", render: defaultRenderer },
    { title: "Đơn vị", dataIndex: "unit", align: "center", render: defaultRenderer },
    { title: "Số lượng", dataIndex: "quantity", align: "center", render: defaultRenderer },
    { title: "Giá tiền", dataIndex: "price", align: "center", render: moneyRenderer },
    { title: "Chi phí", dataIndex: "income", align: "center", render: moneyRenderer },
  ];

  return (
    <StatisticPageLayout title="Thống kê kho" navigatePath="/statistics/inventory-income" handleExport={exportTable}>
      <Card title="Thống kê nhập linh kiện" bordered={false} className="mt-8">
        <Table
          size="small"
          columns={columnsIn}
          rowKey={(record) => record.id}
          dataSource={prodIn}
          loading={loading}
          pagination={{ hideOnSinglePage: true, pageSize: 5, size: "default" }}
          expandable={{
            expandedRowRender: (record) => <TableRowExpand record={record} labels={inLabels} />,
          }}
          scroll={{ x: 576 }}
        />
        <Space className="mt-4 mr-4" style={{ display: "flex", justifyContent: "end" }}>
          <span className="text-semibold">Tổng chi:</span>
          <span>{incomeRenderer(totalLost)}</span>
        </Space>
      </Card>
      <Card title="Thống kê xuất linh kiện" bordered={false} className="mt-8">
        <Table
          size="small"
          columns={columnsOut}
          rowKey={(record) => record.id}
          dataSource={prodOut}
          loading={loading}
          pagination={{ hideOnSinglePage: true, pageSize: 5, size: "default" }}
          expandable={{
            expandedRowRender: (record) => <TableRowExpand record={record} labels={outLabels} />,
          }}
          scroll={{ x: 576 }}
        />
        <Space className="mt-4 mr-4" style={{ display: "flex", justifyContent: "end" }}>
          <span className="text-semibold">Tổng thu:</span>
          <span>{incomeRenderer(totalEarn)}</span>
        </Space>
      </Card>
      <Card bordered={false} className="mt-8">
        <Typography.Title level={4} className="mb-0">
          <Space className="mr-4" style={{ display: "flex", justifyContent: "center" }}>
            <span>Chênh lệch nhập / xuất:</span>
            <span>{incomeRenderer(totalLost + totalEarn)}</span>
          </Space>
        </Typography.Title>
      </Card>
    </StatisticPageLayout>
  );
};

export default InventoryStatistic;
