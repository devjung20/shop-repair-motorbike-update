import { Button, Space, Table } from "antd";
import { DeleteOutlined } from "@ant-design/icons";
import { useEffect, useState } from "react";
import { Link, useLocation, useNavigate } from "react-router-dom";
import AntdSwal from "../../custom/AntdSwal";
import useTableSearch from "../../hooks/useTableSearch";
import ListPageLayout from "../../layouts/ListPageLayout";
import TableRowExpand from "../../components/TableRowExpand";
import {
  accountsIsActiveRenderer,
  dateTimeRenderer,
  defaultRenderer,
  employeeTypeRenderer,
} from "../../utils/renderer";
import queryString from "query-string";
import { apiCaller } from "../../utils/api";
import { Excel } from "antd-table-saveas-excel";
import accountAPI from "../../api/requests/account";

const labels = [
  { title: "Tài khoản", dataIndex: "userName", render: defaultRenderer },
  { title: "Email", dataIndex: "email", render: defaultRenderer },
  { title: "Vai trò", dataIndex: "type", render: employeeTypeRenderer },
  { title: "Trạng thái", dataIndex: "status", render: accountsIsActiveRenderer },
];

const ListAccount = () => {
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
      ...useTableSearch("Tài khoản", params.type),
      defaultFilteredValue: params.type ? [params.type] : null,
      title: "Tài khoản",
      dataIndex: "userName",
      render: (name, record) => <Link to={`/accounts/edit/${record?.id}`}>{name}</Link>,
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
      title: "Vai trò",
      dataIndex: "type",
      align: "center",
      render: employeeTypeRenderer,
      filters: [
        { text: "Quản lý", value: "MANAGER" },
        { text: "NV điều phối", value: "DISPATCHER" },
        { text: "NV sửa chữa", value: "REPAIRER" },
      ],
      defaultFilteredValue: params.role ? [params.role] : null,
      filterMultiple: false,
      onFilter: () => true,
    },
    {
      title: "",
      key: "action",
      align: "right",
      render: (_, record) => (
        <Space size="middle">
          <Button type="link" danger size="large" onClick={() => deleteAccount(record.id)}>
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
      accountAPI.getAll({
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

    navigate("/accounts?" + queryString.stringify(currParams));
  };

  const deleteAccount = (id) => {
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
        apiCaller(accountAPI.deleteById(id), () => {
          AntdSwal.fire("Thành công", "Bạn đã xóa tài khoản thành công", "success");
          navigate(`/accounts?key=${!key}`);
        });
      }
    });
  };

  const exportTable = () => {
    setLoading(true);
    const queryParams = { ...params };
    delete queryParams.key;

    apiCaller(
      accountAPI.getAll({
        ...params,
        pageNumber: 1,
        pageSize: 1000,
      }),
      (data) => {
        new Excel()
          .addSheet("Danh sách tài khoản")
          .addColumns(labels)
          .addDataSource(data.data.content, { str2Percent: true })
          .saveAs(`Danh sách tài khoản - ${dateTimeRenderer()}.xlsx`);
        setLoading(false);
      }
    );
  };

  return (
    <ListPageLayout
      title="Tài khoản"
      cardTitle="Danh sách tài khoản"
      addText="Thêm tài khoản"
      exportText="Xuất danh sách"
      handleAdd={() => navigate("/accounts/add")}
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
        scroll={{ x: 576 }}
        onChange={handleTableChange}
      />
    </ListPageLayout>
  );
};

export default ListAccount;
