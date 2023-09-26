import { Button, Space, Table } from "antd";
import { DeleteOutlined } from "@ant-design/icons";
import { useContext, useEffect, useState } from "react";
import { Link, useLocation, useNavigate } from "react-router-dom";
import AntdSwal from "../../custom/AntdSwal";
import useTableSearch from "../../hooks/useTableSearch";
import ListPageLayout from "../../layouts/ListPageLayout";
import TableRowExpand from "../../components/TableRowExpand";
import { dateTimeRenderer, defaultRenderer, moneyRenderer } from "../../utils/renderer";
import queryString from "query-string";
import { apiCaller } from "../../utils/api";
import { Excel } from "antd-table-saveas-excel";
import serviceAPI from "../../api/requests/service";
import AuthContext from "../../context/AuthContext";

const labels = [
  { title: "Mã dịch vụ", dataIndex: "code", render: defaultRenderer },
  { title: "Dịch vụ", dataIndex: "name", render: defaultRenderer },
  { title: "Giá dịch vụ", dataIndex: "price", render: moneyRenderer },
  { title: "Mô tả", dataIndex: "description", render: defaultRenderer },
];

const ListService = () => {
  const { user } = useContext(AuthContext);
  const isManager = user?.role === "MANAGER";
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
      title: "Dịch vụ",
      dataIndex: "name",
      render: (name, record) => <Link to={`/services/edit/${record?.id}`}>{name}</Link>,
    },
    {
      ...useTableSearch("code", params.code),
      defaultFilteredValue: params.code ? [params.code] : null,
      title: "Mã dịch vụ",
      dataIndex: "code",
      align: "center",
      render: defaultRenderer,
    },
    {
      title: "Giá dịch vụ",
      dataIndex: "price",
      align: "center",
      render: moneyRenderer,
      defaultSortOrder: params.sortBy === "price" ? params.sortOrder : null,
      sortDirections: ["ascend", "descend", "ascend"],
      sorter: (_, __, sortOrder) => sortOrder === "ascend",
    },
    {
      title: "Mô tả",
      dataIndex: "description",
      align: "center",
      render: defaultRenderer,
    },
  ];

  if (isManager) {
    columns.push({
      title: "",
      key: "action",
      align: "right",
      render: (_, record) => (
        <Space size="middle">
          <Button type="link" danger size="large" onClick={() => deleteService(record.id)}>
            <DeleteOutlined />
          </Button>
        </Space>
      ),
    });
  }

  const [dataSource, setDataSource] = useState([]);

  const [loading, setLoading] = useState(false);

  useEffect(() => {
    setLoading(true);
    const queryParams = { ...params };
    delete queryParams.key;

    apiCaller(
      serviceAPI.getAll({
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

    navigate("/services?" + queryString.stringify(currParams));
  };

  const deleteService = (id) => {
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
        apiCaller(serviceAPI.deleteById(id), () => {
          AntdSwal.fire("Thành công", "Bạn đã xóa nhân viên thành công", "success");
          navigate(`/services?key=${!key}`);
        });
      }
    });
  };

  const exportTable = () => {
    setLoading(true);
    const queryParams = { ...params };
    delete queryParams.key;

    apiCaller(
      serviceAPI.getAll({
        ...params,
        pageNumber: 1,
        pageSize: 1000,
      }),
      (data) => {
        new Excel()
          .addSheet("Danh sách dịch vụ")
          .addColumns(labels)
          .addDataSource(data.data.content, { str2Percent: true })
          .saveAs(`Danh sách dịch vụ - ${dateTimeRenderer()}.xlsx`);
        setLoading(false);
      }
    );
  };

  return (
    <ListPageLayout
      title="Dịch vụ"
      cardTitle="Danh sách dịch vụ"
      addable={isManager}
      addText="Thêm mới dịch vụ"
      exportText="Xuất danh sách"
      handleAdd={() => navigate("/services/add")}
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

export default ListService;
