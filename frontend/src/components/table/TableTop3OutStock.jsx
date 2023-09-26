import { CheckCircleOutlined } from "@ant-design/icons";
import { Card, Table, Typography } from "antd";
import { Link } from "react-router-dom";
import BlankContent from "../styled/BlankContent";
import RankingCircle from "../styled/RankingCircle";
const { Title } = Typography;

const TableTop3OutStock = ({ products }) => {
  return (
    <Card
      title={
        <div style={{ display: "flex", justifyContent: "space-between" }}>
          <span>Linh kiện đã hết</span>
          {Boolean(products?.length) && <Link to="/products?status=OUT_STOCK">Xem tất cả</Link>}
        </div>
      }
      className="h-100"
    >
      {Boolean(products?.length) && (
        <Table
          columns={[
            {
              title: "STT",
              dataIndex: "top",
              align: "center",
              render: (top) => <RankingCircle>{top}</RankingCircle>,
            },
            {
              title: "Linh kiện",
              dataIndex: "name",
              align: "center",
              render: (name, record) => (
                <Title level={5} className="line-1">
                  <Link to={`/products/edit/${record.id}`}>{name}</Link>
                </Title>
              ),
            },
            {
              title: "Số lượng",
              dataIndex: "storageQuantity",
              align: "center",
              render: (qty, record) => <Title level={5}>{qty + " " + record?.unit?.toLowerCase()}</Title>,
            },
            {
              title: "Tối thiểu",
              dataIndex: "quantityWarning",
              align: "center",
              render: (qty, record) => <Title level={5}>{qty + " " + record?.unit?.toLowerCase()}</Title>,
            },
          ]}
          rowKey={(record) => record.id}
          dataSource={products}
          size="small"
          bordered
          pagination={{ pageSize: 100, hideOnSinglePage: true }}
        />
      )}

      {Boolean(!products?.length) && (
        <BlankContent iconSize={150} iconColor="#4BB543">
          <CheckCircleOutlined />
          <Title level={5} className="mt-4">
            Các linh kiện hiện tại đều còn hàng
          </Title>
        </BlankContent>
      )}
    </Card>
  );
};

export default TableTop3OutStock;
