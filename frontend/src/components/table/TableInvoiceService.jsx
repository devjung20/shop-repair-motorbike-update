import { Space, Table } from "antd";
import { Link } from "react-router-dom";
import { moneyRenderer } from "../../utils/renderer";

const TableInvoiceService = ({ dataSource = [], loading, totalServicePrice }) => {
  const mappedDataSource = dataSource.map((data) => ({ ...data, totalPrice: data.quantity * data.price }));

  const columns = [
    {
      title: "Dịch vụ",
      dataIndex: "name",
      render: (name, record) => <Link to={`/services/edit/${record?.id}`}>{name}</Link>,
    },
    { title: "Đơn giá", dataIndex: "price", align: "center", render: moneyRenderer },
    { title: "Số lượng", dataIndex: "quantity", align: "center" },
    { title: "Thành tiền", dataIndex: "totalPrice", align: "center", render: moneyRenderer },
  ];

  return (
    <>
      <Table
        size="small"
        columns={columns}
        rowKey={(record) => record.id}
        dataSource={mappedDataSource}
        pagination={{ hideOnSinglePage: true }}
        loading={loading}
        scroll={{ x: 576 }}
      />
      <Space className="mt-4 mr-4" style={{ display: "flex", justifyContent: "end" }}>
        <span className="text-semibold">Tổng cộng:</span>
        <span>{moneyRenderer(totalServicePrice)}</span>
      </Space>
    </>
  );
};

export default TableInvoiceService;
