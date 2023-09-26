import { Button, Space, Table } from "antd";
import { DeleteOutlined } from "@ant-design/icons";
import { useEffect, useState } from "react";
import { Link, useLocation, useNavigate } from "react-router-dom";
import AntdSwal from "../../custom/AntdSwal";
import useTableSearch from "../../hooks/useTableSearch";
import ListPageLayout from "../../layouts/ListPageLayout";
import TableRowExpand from "../../components/TableRowExpand";
import { dateTimeRenderer, defaultRenderer } from "../../utils/renderer";
import queryString from "query-string";
import { apiCaller } from "../../utils/api";
import orderAPI from "../../api/requests/order";
import { Excel } from "antd-table-saveas-excel";

const labels = [
  { title: "Mã phiếu", dataIndex: "code", render: defaultRenderer },
  { title: "Mã khách hàng", dataIndex: "customerCode", render: defaultRenderer },
  { title: "Tên khách hàng", dataIndex: "customerName", render: defaultRenderer },
  { title: "NV điều phối", dataIndex: "dispatcherName", render: defaultRenderer },
  { title: "NV sửa chữa", dataIndex: "repairerName", render: defaultRenderer },
  { title: "Biển số xe", dataIndex: "motorbikeCode", render: defaultRenderer },
  { title: "Thời gian tạo", dataIndex: "createDate", render: dateTimeRenderer },
];

const ListOrder = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const params = queryString.parse(location.search);

  const key = params.key || 0;

  const [pagination, setPagination] = useState({
    size: "default",
    hideOnSinglePage: true,
    showTotal: (total, range) => `Hiển thị ${range[0]} - ${range[1]} trên tổng số ${total}`,
    responsive: true,
    showLessItem: true,
  });

  const columns = [
    {
      ...useTableSearch("code", params.code),
      defaultFilteredValue: params.code ? [params.code] : null,
      title: "Mã phiếu",
      dataIndex: "code",
      render: (code, record) => <Link to={`/orders/edit/${record?.id}`}>{code}</Link>,
    },
    {
      ...useTableSearch("customerCode", params.customerCode),
      defaultFilteredValue: params.customerCode ? [params.customerCode] : null,
      title: "Mã KH",
      dataIndex: "customerCode",
      align: "center",
      render: defaultRenderer,
    },
    {
      ...useTableSearch("customerName", params.customerName),
      defaultFilteredValue: params.customerName ? [params.customerName] : null,
      title: "Tên KH",
      dataIndex: "customerName",
      align: "center",
      render: defaultRenderer,
    },
    {
      ...useTableSearch("dispatcherName", params.dispatcherName),
      defaultFilteredValue: params.dispatcherName ? [params.dispatcherName] : null,
      title: "NV điều phối",
      dataIndex: "dispatcherName",
      align: "center",
      render: defaultRenderer,
    },
    {
      ...useTableSearch("repairerName", params.repairerName),
      defaultFilteredValue: params.repairerName ? [params.repairerName] : null,
      title: "NV sửa chữa",
      dataIndex: "repairerName",
      align: "center",
      render: defaultRenderer,
    },
    {
      ...useTableSearch("motorbikeCode", params.motorbikeCode),
      defaultFilteredValue: params.motorbikeCode ? [params.motorbikeCode] : null,
      title: "Biển số xe",
      dataIndex: "motorbikeCode",
      align: "center",
      render: defaultRenderer,
    },
    {
      title: "Thời gian tạo",
      dataIndex: "createDate",
      align: "center",
      render: dateTimeRenderer,
      defaultSortOrder: params.sortBy === "createDate" ? params.sortOrder : null,
      sortDirections: ["ascend", "descend", "ascend"],
      sorter: (_, __, sortOrder) => sortOrder === "ascend",
    },
    {
      title: "",
      key: "action",
      align: "right",
      render: (_, record) => (
        <Space size="middle">
          <Button type="link" danger size="large" onClick={() => deleteOrder(record.id)}>
            <DeleteOutlined />
          </Button>
        </Space>
      ),
    },
  ];

  const [dataSource, setDataSource] = useState([]);

  const [loading, setLoading] = useState(false);

  useEffect(() => {
    setLoading(true);
    const queryParams = { ...params };
    delete queryParams.key;

    apiCaller(
      orderAPI.getAll({
        ...params,
        pageNumber: params.pageNumber || 1,
        pageSize: params.pageSize || 5,
        type: "ORDER",
      }),
      (data) => {
        setDataSource(data.data.content);
        setPagination({
          ...pagination,
          current: data.data.number + 1,
          pageSize: data.data.size,
          total: data.data.totalElements,
        });
        setLoading(false);
      }
    );
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [key, JSON.stringify(params), JSON.stringify(pagination)]);

  const handleTableChange = (pagination, filters, sorter) => {
    const currParams = { ...params };

    // Apply new page number
    currParams.pageNumber = pagination.current;
    currParams.pageSize = pagination.pageSize;

    // Apply new search params
    Object.keys(filters).forEach((key) => {
      if (filters[key]) {
        currParams[key] = filters[key][0];
      } else {
        delete currParams[key];
      }
    });

    // Apply new sort params
    if (sorter.field && sorter.order) {
      currParams.sortBy = sorter.field;
      currParams.sortOrder = sorter.order;
    }

    navigate("/orders?" + queryString.stringify(currParams));
  };

  const deleteOrder = (id) => {
    AntdSwal.fire({
      title: "Hãy thận trọng!",
      text: "Bạn không thể hoàn tác sau khi đã xác nhận!",
      icon: "warning",
      showCancelButton: true,
      confirmButtonText: "Xác nhận xóa",
      cancelButtonText: "Hủy thao tác",
      reverseButtons: true,
    }).then((result) => {
      if (result.isConfirmed) {
        apiCaller(orderAPI.deleteById(id), () => {
          AntdSwal.fire("Thành công", "Bạn đã xóa phiếu sửa chữa thành công", "success");
          navigate(`/orders?key=${!key}`);
        });
      }
      // else if (result.dismiss === AntdSwal.DismissReason.cancel) {
      //   AntdSwal.fire("Đã hủy", "Bạn đã hủy thao tác", "error");
      // }
    });
  };

  const exportTable = () => {
    setLoading(true);
    const queryParams = { ...params };
    delete queryParams.key;

    apiCaller(
      orderAPI.getAll({
        ...params,
        pageNumber: 1,
        pageSize: 1000,
        type: "ORDER",
      }),
      (data) => {
        new Excel()
          .addSheet("Danh sách phiếu chưa thanh toán")
          .addColumns(labels)
          .addDataSource(data.data.content, { str2Percent: true })
          .saveAs(`Danh sách phiếu chưa thanh toán - ${dateTimeRenderer()}.xlsx`);

        setLoading(false);
      }
    );
  };

  return (
    <ListPageLayout
      title="Phiếu sửa chữa"
      cardTitle="Danh sách phiếu chưa thanh toán"
      addText="Thêm phiếu mới"
      exportText="Xuất danh sách"
      handleAdd={() => navigate("/orders/add")}
      handleExport={exportTable}
    >
      <Table
        size="small"
        columns={columns}
        rowKey={(record) => record.id}
        dataSource={dataSource}
        pagination={pagination}
        loading={loading}
        expandable={{
          expandedRowRender: (record) => <TableRowExpand record={record} labels={labels} />,
        }}
        scroll={{
          x: 576,
        }}
        onChange={handleTableChange}
      />
    </ListPageLayout>
  );
};

export default ListOrder;
