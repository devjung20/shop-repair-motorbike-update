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
import { Excel } from "antd-table-saveas-excel";
import customerAPI from "../../api/requests/customer";

const labels = [
  { title: "Mã khách hàng", dataIndex: "code", render: defaultRenderer },
  { title: "Tên khách hàng", dataIndex: "name", render: defaultRenderer },
  { title: "Số điện thoại", dataIndex: "phone", render: defaultRenderer },
  { title: "Email", dataIndex: "email", render: defaultRenderer },
  { title: "Địa chỉ", dataIndex: "address", render: defaultRenderer },
];

const ListCustomer = () => {
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
      ...useTableSearch("name", params.name),
      defaultFilteredValue: params.name ? [params.name] : null,
      title: "Khách hàng",
      dataIndex: "name",
      render: (name, record) => <Link to={`/customers/edit/${record?.id}`}>{name}</Link>,
    },
    {
      ...useTableSearch("code", params.code),
      defaultFilteredValue: params.code ? [params.code] : null,
      title: "Mã KH",
      dataIndex: "code",
      align: "center",
      render: defaultRenderer,
    },
    {
      ...useTableSearch("phone", params.phone),
      defaultFilteredValue: params.phone ? [params.phone] : null,
      title: "Số điện thoại",
      dataIndex: "phone",
      align: "center",
      render: defaultRenderer,
    },
    {
      ...useTableSearch("email", params.email),
      defaultFilteredValue: params.email ? [params.email] : null,
      title: "Email",
      dataIndex: "email",
      align: "center",
      render: defaultRenderer,
    },
    {
      title: "",
      key: "action",
      align: "right",
      render: (_, record) => (
        <Space size="middle">
          <Button type="link" danger size="large" onClick={() => deleteCustomer(record.id)}>
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
      customerAPI.getAll({
        ...params,
        pageNumber: params.pageNumber || 1,
        pageSize: params.pageSize || 5,
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

    navigate("/customers?" + queryString.stringify(currParams));
  };

  const deleteCustomer = (id) => {
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
        apiCaller(customerAPI.deleteById(id), () => {
          AntdSwal.fire("Thành công", "Bạn đã xóa khách hàng thành công", "success");
          navigate(`/customers?key=${!key}`);
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
      customerAPI.getAll({
        ...params,
        pageNumber: 1,
        pageSize: 1000,
      }),
      (data) => {
        new Excel()
          .addSheet("Danh sách khách hàng")
          .addColumns(labels)
          .addDataSource(data.data.content, { str2Percent: true })
          .saveAs(`Danh sách khách hàng - ${dateTimeRenderer()}.xlsx`);

        setLoading(false);
      }
    );
  };

  return (
    <ListPageLayout
      title="Khách hàng"
      cardTitle="Danh sách khách hàng"
      addText="Thêm khách hàng"
      exportText="Xuất danh sách"
      handleAdd={() => navigate("/customers/add")}
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

export default ListCustomer;
