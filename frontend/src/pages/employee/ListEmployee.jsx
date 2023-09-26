import { Button, Space, Table, Tag } from "antd";
import { DeleteOutlined } from "@ant-design/icons";
import { useContext, useEffect, useState } from "react";
import { Link, useLocation, useNavigate } from "react-router-dom";
import AntdSwal from "../../custom/AntdSwal";
import useTableSearch from "../../hooks/useTableSearch";
import ListPageLayout from "../../layouts/ListPageLayout";
import TableRowExpand from "../../components/TableRowExpand";
import {
  dateRenderer,
  dateTimeRenderer,
  defaultRenderer,
  employeeStatusRenderer,
  employeeTypeRenderer,
  genderRenderer,
} from "../../utils/renderer";
import queryString from "query-string";
import { apiCaller } from "../../utils/api";
import employeeAPI from "../../api/requests/employee";
import { Excel } from "antd-table-saveas-excel";
import AuthContext from "../../context/AuthContext";

const labels = [
  { title: "Mã nhân viên", dataIndex: "code", render: defaultRenderer },
  { title: "Tên nhân viên", dataIndex: "name", render: defaultRenderer },
  { title: "Ngày sinh", dataIndex: "birthday", render: dateRenderer },
  { title: "Số điện thoại", dataIndex: "phone", render: defaultRenderer },
  { title: "Email", dataIndex: "email", render: defaultRenderer },
  { title: "Địa chỉ", dataIndex: "address", render: defaultRenderer },
  { title: "Giới tính", dataIndex: "gender", render: genderRenderer },
  { title: "Chức vụ", dataIndex: "type", render: employeeTypeRenderer },
  { title: "Trạng thái", dataIndex: "status", render: employeeStatusRenderer },
];

const ListEmployee = () => {
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
      title: "Nhân viên",
      dataIndex: "name",
      render: (name, record) => <Link to={`/employees/edit/${record?.id}`}>{name}</Link>,
    },
    {
      ...useTableSearch("code", params.code),
      defaultFilteredValue: params.code ? [params.code] : null,
      title: "Mã NV",
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
      title: "Chức vụ",
      defaultFilteredValue: params.type ? [params.type] : null,
      dataIndex: "type",
      align: "center",
      render: employeeTypeRenderer,
      filters: [
        { text: "Quản lý", value: "MANAGER" },
        { text: "NV điều phối", value: "DISPATCHER" },
        { text: "NV sửa chữa", value: "REPAIRER" },
      ],
      filterMultiple: false,
      onFilter: () => true,
    },
    // {
    //   title: "Lương cơ bản",
    //   dataIndex: "salary",
    //   align: "center",
    //   render: moneyRenderer,
    //   defaultSortOrder: params.sortBy === "salary" ? params.sortOrder : null,
    //   sortDirections: ["ascend", "descend", "ascend"],
    //   sorter: (_, __, sortOrder) => sortOrder === "ascend",
    // },
    {
      title: "Trạng thái",
      defaultFilteredValue: params.status ? [params.status] : null,
      dataIndex: "status",
      align: "center",
      render: (status) => (
        <Tag color={status ? "#66B8FF" : "error"} key={Boolean(status)} style={{ borderRadius: 50 }}>
          {employeeStatusRenderer(status)}
        </Tag>
      ),
      filters: [
        { text: "Đang làm việc", value: "true" },
        { text: "Đã nghỉ việc", value: "false" },
      ],
      filterMultiple: false,
      onFilter: () => true,
    },
  ];

  if (isManager) {
    columns.push({
      title: "",
      key: "action",
      align: "right",
      render: (_, record) => (
        <Space size="middle">
          <Button type="link" danger size="large" onClick={() => deleteEmployee(record.id)}>
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
      employeeAPI.getAll({
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

    navigate("/employees?" + queryString.stringify(currParams));
  };

  const deleteEmployee = (id) => {
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
        apiCaller(employeeAPI.deleteById(id), () => {
          AntdSwal.fire("Thành công", "Bạn đã xóa nhân viên thành công", "success");
          navigate(`/employees?key=${!key}`);
        });
      }
    });
  };

  const exportTable = () => {
    setLoading(true);
    const queryParams = { ...params };
    delete queryParams.key;

    apiCaller(
      employeeAPI.getAll({
        ...params,
        pageNumber: 1,
        pageSize: 1000,
      }),
      (data) => {
        new Excel()
          .addSheet("Danh sách nhân viên")
          .addColumns(labels)
          .addDataSource(data.data.content, { str2Percent: true })
          .saveAs(`Danh sách nhân viên - ${dateTimeRenderer()}.xlsx`);
        setLoading(false);
      }
    );
  };

  return (
    <ListPageLayout
      title="Nhân viên"
      cardTitle="Danh sách nhân viên"
      addText="Thêm nhân viên"
      exportText="Xuất danh sách"
      addable={isManager}
      handleAdd={() => navigate("/employees/add")}
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

export default ListEmployee;
