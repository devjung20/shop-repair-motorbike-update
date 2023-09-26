import { Button, Space, Table, Tag } from "antd";
import { DeleteOutlined } from "@ant-design/icons";
import { useContext, useEffect, useState } from "react";
import { Link, useLocation, useNavigate } from "react-router-dom";
import AntdSwal from "../../custom/AntdSwal";
import useTableSearch from "../../hooks/useTableSearch";
import ListPageLayout from "../../layouts/ListPageLayout";
import TableRowExpand from "../../components/TableRowExpand";
import { dateTimeRenderer, defaultRenderer, moneyRenderer, productStatusRenderer } from "../../utils/renderer";
import queryString from "query-string";
import { apiCaller } from "../../utils/api";
import productAPI from "../../api/requests/product";
import { Excel } from "antd-table-saveas-excel";
import AuthContext from "../../context/AuthContext";

const labels = [
  { title: "Mã sản phẩm", dataIndex: "code", render: defaultRenderer },
  { title: "Tên sản phẩm", dataIndex: "name", render: defaultRenderer },
  { title: "Thương hiệu", dataIndex: "brand", render: defaultRenderer },
  { title: "Giá nhập", dataIndex: "priceIn", render: moneyRenderer },
  { title: "Giá bán", dataIndex: "priceOut", render: moneyRenderer },
  { title: "Đơn vị", dataIndex: "unit", render: defaultRenderer },
  { title: "Số lượng tồn", dataIndex: "storageQuantity", render: defaultRenderer },
  { title: "Trạng thái", dataIndex: "storageQuantity", render: productStatusRenderer },
  { title: "Mô tả", dataIndex: "description", render: defaultRenderer },
];

const ListProduct = () => {
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
      title: "Linh kiện",
      dataIndex: "name",
      render: (name, record) => <Link to={`/products/edit/${record?.id}`}>{name}</Link>,
    },
    {
      ...useTableSearch("code", params.code),
      defaultFilteredValue: params.code ? [params.code] : null,
      title: "Mã LK",
      dataIndex: "code",
      align: "center",
      render: defaultRenderer,
    },
    {
      title: "Số lượng tồn",
      dataIndex: "storageQuantity",
      align: "center",
      defaultSortOrder: params.sortBy === "storageQuantity" ? params.sortOrder : null,
      sortDirections: ["ascend", "descend", "ascend"],
      sorter: (_, __, sortOrder) => sortOrder === "ascend",
      render: defaultRenderer,
    },
    {
      title: "Đơn vị",
      dataIndex: "unit",
      align: "center",
      render: defaultRenderer,
    },
    {
      title: "Giá nhập",
      dataIndex: "priceIn",
      align: "center",
      render: moneyRenderer,
      defaultSortOrder: params.sortBy === "priceIn" ? params.sortOrder : null,
      sortDirections: ["ascend", "descend", "ascend"],
      sorter: (_, __, sortOrder) => sortOrder === "ascend",
    },
    {
      title: "Giá bán",
      dataIndex: "priceOut",
      align: "center",
      render: moneyRenderer,
      defaultSortOrder: params.sortBy === "priceOut" ? params.sortOrder : null,
      sortDirections: ["ascend", "descend", "ascend"],
      sorter: (_, __, sortOrder) => sortOrder === "ascend",
    },
    {
      title: "Trạng thái",
      defaultFilteredValue: params.status ? [params.status] : null,
      dataIndex: "status",
      align: "center",
      render: (status) => (
        <Tag
          color={
            {
              IN_STOCK: "processing",
              OUT_STOCK: "error",
              LOW_STOCK: "warning",
            }[status]
          }
          key={status}
          style={{ borderRadius: 50 }}
        >
          {productStatusRenderer(status)}
        </Tag>
      ),
      filters: [
        { text: "Còn hàng", value: "IN_STOCK" },
        { text: "Hết hàng", value: "OUT_STOCK" },
        { text: "Sắp hết", value: "LOW_STOCK" },
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
          <Button type="link" danger size="large" onClick={() => deleteProduct(record.id)}>
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
      productAPI.getAll({
        ...params,
        pageNumber: params.pageNumber || 1,
        pageSize: params.pageSize || 5,
      }),
      (data) => {
        setDataSource(data?.data?.content);
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
    if (currParams.storageQuantity) {
      currParams.status = currParams.storageQuantity;
      delete currParams.storageQuantity;
    }
    // Apply new sort params
    if (sorter.field && sorter.order) {
      currParams.sortBy = sorter.field;
      currParams.sortOrder = sorter.order;
    }

    navigate("/products?" + queryString.stringify(currParams));
  };

  const deleteProduct = (id) => {
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
        apiCaller(productAPI.deleteById(id), () => {
          AntdSwal.fire("Thành công", "Bạn đã xóa nhân viên thành công", "success");
          navigate(`/products?key=${!key}`);
        });
      }
    });
  };

  const exportTable = () => {
    setLoading(true);
    const queryParams = { ...params };
    delete queryParams.key;

    apiCaller(
      productAPI.getAll({
        ...params,
        pageNumber: 1,
        pageSize: 1000,
      }),
      (data) => {
        new Excel()
          .addSheet("Danh sách linh kiện")
          .addColumns(labels)
          .addDataSource(data.data.content, { str2Percent: true })
          .saveAs(`Danh sách linh kiện - ${dateTimeRenderer()}.xlsx`);
        setLoading(false);
      }
    );
  };

  return (
    <ListPageLayout
      title="Linh kiện"
      cardTitle="Danh sách linh kiện"
      addable={isManager}
      addText="Thêm linh kiện"
      exportText="Xuất danh sách"
      handleAdd={() => navigate("/products/add")}
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

export default ListProduct;
