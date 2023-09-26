import { Card, Table, Typography } from "antd";
import BlankContent from "../styled/BlankContent";
import { BarChartOutlined } from "@ant-design/icons";
import RankingCircle from "../styled/RankingCircle";
import { Link, useLocation } from "react-router-dom";
const { Title } = Typography;

const TableTop5Service = ({ services = [], className }) => {
  const location = useLocation();
  const hideViewMore = location?.pathname === "/statistics/services-income";

  return (
    <Card
      className={`${className} h-100`}
      title={
        <div style={{ display: "flex", justifyContent: "space-between" }}>
          <span>Top dịch vụ sử dụng nhiều</span>
          {!hideViewMore && <Link to="/statistics/services-income">Xem thêm</Link>}
        </div>
      }
    >
      {Boolean(services?.length) && (
        <Table
          columns={[
            {
              title: "Top",
              dataIndex: "top",
              align: "center",
              render: (name) => <RankingCircle>{name}</RankingCircle>,
            },
            {
              title: "Dịch vụ",
              dataIndex: "name",
              align: "center",
              render: (name) => (
                <Title level={5} className="line-1">
                  {name}
                </Title>
              ),
            },
            {
              title: "Số lượng",
              dataIndex: "quantity",
              align: "center",
              render: (qty) => <Title level={5}>{qty}</Title>,
            },
          ]}
          rowKey={(record) => record.top}
          dataSource={services}
          size="small"
          bordered
          pagination={{ pageSize: 5, hideOnSinglePage: true, size: "default" }}
        />
      )}
      {Boolean(!services?.length) && (
        <BlankContent>
          <BarChartOutlined />
          <Title level={5} className="mt-4">
            Hiện chưa có thống kê về dịch vụ
          </Title>
        </BlankContent>
      )}
    </Card>
  );
};

export default TableTop5Service;
