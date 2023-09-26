import { Card, Table, Typography } from "antd";
import BlankContent from "../styled/BlankContent";
import { BarChartOutlined } from "@ant-design/icons";
import RankingCircle from "../styled/RankingCircle";
import { Link, useLocation } from "react-router-dom";
const { Title } = Typography;

const TableTop5Product = ({ products = [], className = "" }) => {
  const location = useLocation();
  const hideViewMore = location?.pathname === "/statistics/products-income";

  return (
    <Card
      className={`${className} h-100`}
      title={
        <div style={{ display: "flex", justifyContent: "space-between" }}>
          <span>Top linh kiện sử dụng nhiều</span>
          {!hideViewMore && <Link to="/statistics/products-income">Xem thêm</Link>}
        </div>
      }
    >
      {Boolean(products?.length) && (
        <Table
          size="small"
          columns={[
            {
              title: "Top",
              dataIndex: "top",
              align: "center",
              render: (name) => <RankingCircle>{name}</RankingCircle>,
            },
            {
              title: "Linh kiện",
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
            {
              title: "Đơn vị",
              dataIndex: "unit",
              align: "center",
              render: (unit) => <Title level={5}>{unit}</Title>,
            },
          ]}
          rowKey={(record) => record.top}
          dataSource={products}
          bordered
          pagination={{ pageSize: 5, hideOnSinglePage: true, size: "default" }}
        />
      )}
      {Boolean(!products?.length) && (
        <BlankContent>
          <BarChartOutlined />
          <Title level={5} className="mt-4">
            Hiện chưa có thống kê về linh kiện
          </Title>
        </BlankContent>
      )}
    </Card>
  );
};

export default TableTop5Product;
