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
import TableTop5Service from "../../components/table/TableTop5Service";

const labels = [
  { title: "Dịch vụ", dataIndex: "name", render: defaultRenderer },
  { title: "Mã dịch vụ", dataIndex: "code", render: defaultRenderer },
  { title: "Giá dịch vụ", dataIndex: "price", render: moneyRenderer },
  { title: "Số lượng", dataIndex: "quantity", render: defaultRenderer },
  { title: "Lợi nhuận", dataIndex: "income", render: moneyRenderer },
];

const ServiceStatistic = () => {
  const location = useLocation();
  const params = queryString.parse(location.search);
  const { startTime, endTime } = params;

  const [serviceTop, setServiceTop] = useState([]);
  const [serviceStats, setServiceStats] = useState([]);
  const [loading, setLoading] = useState(false);

  const totalEarn = useMemo(() => serviceStats.reduce((sum, prod) => (sum += prod.income), 0), [serviceStats]);

  useEffect(() => {
    setLoading(true);

    const baseParams = {
      startTime: toYYYY_MM_DD(startTime || dayjs().subtract(1, "month")),
      endTime: toYYYY_MM_DD(endTime),
      pageSize: 1000,
      pageNumber: 1,
    };

    Promise.all([statisticAPI.getTopServiceStatistic(baseParams), statisticAPI.getServiceStatistic(baseParams)])
      .then(([serTopRes, serRes]) => {
        setServiceTop(serTopRes?.data?.data?.map((p, i) => ({ ...p, top: i + 1 })));
        setServiceStats(serRes?.data?.data?.content?.map((s, i) => ({ ...s, id: i + 1 })));
        setLoading(false);
      })
      .catch((err) => console.log(err));
  }, [endTime, startTime]);

  const exportTable = useCallback(() => {
    new Excel()
      .addSheet("Lợi nhuận từ dịch vụ")
      .addColumns(labels)
      .addDataSource(serviceStats, { str2Percent: true })
      .saveAs(`Thống kê dịch vụ - (${toDDMMYYYY(startTime)} - ${toDDMMYYYY(endTime)}).xlsx`);
  }, [serviceStats, startTime, endTime]);

  const columns = [
    {
      title: "Dịch vụ",
      dataIndex: "name",
      render: (name, record) => <Link to={`/services/edit/${record?.id}`}>{name}</Link>,
    },
    { title: "Mã dịch vụ", dataIndex: "code", align: "center", render: defaultRenderer },
    { title: "Giá dịch vụ", dataIndex: "price", align: "center", render: moneyRenderer },
    { title: "Số lượng", dataIndex: "quantity", align: "center", render: defaultRenderer },
    { title: "Lợi nhuận", dataIndex: "income", align: "center", render: moneyRenderer },
  ];

  return (
    <StatisticPageLayout title="Thống kê dịch vụ" navigatePath="/statistics/services-income" handleExport={exportTable}>
      <TableTop5Service services={serviceTop} className="mt-8" />
      <Card title="Lợi nhuận từ dịch vụ" bordered={false} className="mt-8">
        <Table
          size="small"
          columns={columns}
          rowKey={(record) => record.id}
          dataSource={serviceStats}
          loading={loading}
          pagination={{ hideOnSinglePage: true, pageSize: 5, size: "default" }}
          expandable={{
            expandedRowRender: (record) => <TableRowExpand record={record} labels={labels} />,
          }}
          scroll={{ x: 576 }}
        />
        <Space className="mt-4 mr-4" style={{ display: "flex", justifyContent: "end" }}>
          <span className="text-semibold">Tổng cộng:</span>
          <span>{incomeRenderer(totalEarn)}</span>
        </Space>
      </Card>
      <Card bordered={false} className="mt-8">
        <Typography.Title level={4} className="mb-0">
          <Space className="mr-4" style={{ display: "flex", justifyContent: "center" }}>
            <span>Lợi nhuận từ dịch vụ:</span>
            <span>{incomeRenderer(totalEarn)}</span>
          </Space>
        </Typography.Title>
      </Card>
    </StatisticPageLayout>
  );
};

export default ServiceStatistic;
