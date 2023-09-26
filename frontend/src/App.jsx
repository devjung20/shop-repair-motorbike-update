import { HashRouter, Route, Routes } from "react-router-dom";
import "./App.css";

import AppLayout from "./layouts/AppLayout";
import NotFound from "./pages/NotFound";
import { useContext } from "react";
import AuthContext from "./context/AuthContext";

import ListEmployee from "./pages/employee/ListEmployee";
import AddEmployee from "./pages/employee/AddEmployee";
import EditEmployee from "./pages/employee/EditEmployee";

import LoginAndForgot from "./pages/Auth";

import ListProduct from "./pages/product/ListProduct";
import AddProduct from "./pages/product/AddProduct";
import EditProduct from "./pages/product/EditPtoduct";

import ListAccount from "./pages/account/ListAccount";
import AddAccount from "./pages/account/AddAccount";
import EditAccount from "./pages/account/EditAccount";

import ListService from "./pages/service/ListService";
import AddService from "./pages/service/AddService";
import EditService from "./pages/service/EditService";

import ImportProduct from "./pages/product/ImportProduct";
import ExportProduct from "./pages/product/ExportProduct";
import HistoryProduct from "./pages/product/HistoryProduct";

import ListCustomer from "./pages/customer/ListCustomer";
import AddCustomer from "./pages/customer/AddCustomer";
import EditCustomer from "./pages/customer/EditCustomer";

import AddOrder from "./pages/order/AddOrder";
import EditOrder from "./pages/order/EditOrder";
import ListOrder from "./pages/order/ListOrder";

import ListInvoice from "./pages/invoice/ListInvoice";
import DetailInvoice from "./pages/invoice/DetailInvoice";

import ProductStatistic from "./pages/statistic/ProductStatistic";
import ServiceStatistic from "./pages/statistic/ServiceStatistic";
import SettingStore from "./pages/store/SettingStore";
import Dashboard from "./pages/Dashboard";
import InventoryStatistic from "./pages/statistic/InventoryStatistic";

function App() {
  const { user, isLoggedIn } = useContext(AuthContext);
  const { role } = user;

  return (
    <HashRouter>
      {!isLoggedIn && (
        <Routes>
          <Route path="/auth/:action" element={<LoginAndForgot />} />
          <Route path="*" element={<LoginAndForgot />} />
        </Routes>
      )}
      {isLoggedIn && (
        <AppLayout>
          <Routes>
            <Route path="/" element={<Dashboard />} />

            <Route path="/invoices" element={<ListInvoice />} />
            <Route path="/invoices/:invoiceId" element={<DetailInvoice />} />

            <Route path="/services" element={<ListService />} />
            <Route path="/products" element={<ListProduct />} />
            <Route path="/employees" element={<ListEmployee />} />

            <Route path="/customers" element={<ListCustomer />} />
            <Route path="/customers/add" element={<AddCustomer />} />
            <Route path="/customers/edit/:customerId" element={<EditCustomer />} />

            <Route path="/orders" element={<ListOrder />} />
            <Route path="/orders/add" element={<AddOrder />} />
            <Route path="/orders/edit/:orderId" element={<EditOrder />} />

            <Route path="/stock-history" element={<HistoryProduct />} />
            <Route path="/settings" element={<SettingStore />} />
            <Route path="*" element={<NotFound />} />

            {role === "MANAGER" && (
              <>
                <Route path="/statistics/services-income" element={<ServiceStatistic />} />
                <Route path="/statistics/products-income" element={<ProductStatistic />} />
                <Route path="/statistics/inventory-income" element={<InventoryStatistic />} />

                <Route path="/services/add" element={<AddService />} />
                <Route path="/services/edit/:serviceId" element={<EditService />} />

                <Route path="/employees/add" element={<AddEmployee />} />
                <Route path="/employees/edit/:employeeId" element={<EditEmployee />} />

                <Route path="/accounts" element={<ListAccount />} />
                <Route path="/accounts/add" element={<AddAccount />} />
                <Route path="/accounts/edit/:accountId" element={<EditAccount />} />

                <Route path="/products/add" element={<AddProduct />} />
                <Route path="/products/edit/:productId" element={<EditProduct />} />

                <Route path="/stock-in" element={<ImportProduct />} />
                <Route path="/stock-out" element={<ExportProduct />} />
              </>
            )}
          </Routes>
        </AppLayout>
      )}
    </HashRouter>
  );
}

export default App;
