import React from "react";
import {
    Nav,
    NavLink,
    Bars,
    NavMenu,
    NavBtn,
    NavBtnLink,
    Title,
} from "./navbarElements";
import { Outlet, Link } from "react-router-dom";

const Navbar = () => {
    return (
        <>
          <Title to="/">
          🎓git-diploma
          </Title>
          <Nav>
            <Bars />
              <NavMenu>
                <NavLink to="/" >
                  home
                </NavLink>
                <NavLink to="/equipe">
                  équipe
                </NavLink>
                {/* Second Nav */}
                {/* <NavBtnLink to='/sign-in'>Sign In</NavBtnLink> */}
              </NavMenu>
              <NavBtn>
                <NavBtnLink to="/login">
                  sign in
                </NavBtnLink>
              </NavBtn>
          </Nav>
        </>
    );
};

export default Navbar;