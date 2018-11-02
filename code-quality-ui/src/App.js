import React, { Component } from 'react';
import logo from './logo.svg';
import './App.css';
import Button from '@material-ui/core/Button';
import PropTypes from 'prop-types';
import classNames from 'classnames';
import { withStyles } from '@material-ui/core/styles';
import CssBaseline from '@material-ui/core/CssBaseline';
import Drawer from '@material-ui/core/Drawer';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import List from '@material-ui/core/List';
import Typography from '@material-ui/core/Typography';
import Divider from '@material-ui/core/Divider';
import IconButton from '@material-ui/core/IconButton';
import Badge from '@material-ui/core/Badge';
import MenuIcon from '@material-ui/icons/Menu';
import ChevronLeftIcon from '@material-ui/icons/ChevronLeft';
import NotificationsIcon from '@material-ui/icons/Notifications';
import AddIcon from '@material-ui/icons/Add';
import EditIcon from '@material-ui/icons/Edit';
import Icon from '@material-ui/core/Icon';
import DeleteIcon from '@material-ui/icons/Delete';
import ScanIcon from '@material-ui/icons/PlayCircleFilled';
import NavigationIcon from '@material-ui/icons/Navigation';
import SearchIcon from '@material-ui/icons/Search';
import InputBase from '@material-ui/core/InputBase';
import ListItem from '@material-ui/core/ListItem';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import ListItemText from '@material-ui/core/ListItemText';
import ListItemSecondaryAction from '@material-ui/core/ListItemSecondaryAction';
import ListSubheader from '@material-ui/core/ListSubheader';
import DashboardIcon from '@material-ui/icons/Dashboard';
import ShoppingCartIcon from '@material-ui/icons/ShoppingCart';
import PeopleIcon from '@material-ui/icons/People';
import BarChartIcon from '@material-ui/icons/BarChart';
import LayersIcon from '@material-ui/icons/Layers';
import AssignmentIcon from '@material-ui/icons/Assignment';
import ProgressIcon from '@material-ui/icons/Timelapse';
import ReportIcon from '@material-ui/icons/BubbleChart';
import CancelIcon from '@material-ui/icons/Cancel';

const drawerWidth = 240;
const API_URL = 'http://localhost:9000';

const styles = theme => ({
  root: {
    display: 'flex',
  },
  toolbar: {
    paddingRight: 24, // keep right padding when drawer closed
  },
  toolbarIcon: {
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'flex-end',
    padding: '0 8px',
    ...theme.mixins.toolbar,
  },
  appBar: {
    zIndex: theme.zIndex.drawer + 1,
    transition: theme.transitions.create(['width', 'margin'], {
      easing: theme.transitions.easing.sharp,
      duration: theme.transitions.duration.leavingScreen,
    }),
  },
  appBarShift: {
    marginLeft: drawerWidth,
    width: `calc(100% - ${drawerWidth}px)`,
    transition: theme.transitions.create(['width', 'margin'], {
      easing: theme.transitions.easing.sharp,
      duration: theme.transitions.duration.enteringScreen,
    }),
  },
  menuButton: {
    marginLeft: 12,
    marginRight: 36,
  },
  menuButtonHidden: {
    display: 'none',
  },
  title: {
    flexGrow: 1,
  },
  drawerPaper: {
    position: 'relative',
    whiteSpace: 'nowrap',
    width: drawerWidth,
    transition: theme.transitions.create('width', {
      easing: theme.transitions.easing.sharp,
      duration: theme.transitions.duration.enteringScreen,
    }),
  },
  drawerPaperClose: {
    overflowX: 'hidden',
    transition: theme.transitions.create('width', {
      easing: theme.transitions.easing.sharp,
      duration: theme.transitions.duration.leavingScreen,
    }),
    width: theme.spacing.unit * 7,
    [theme.breakpoints.up('sm')]: {
      width: theme.spacing.unit * 9,
    },
  },
  appBarSpacer: theme.mixins.toolbar,
  content: {
    flexGrow: 1,
    padding: theme.spacing.unit * 3,
    height: '100vh',
    overflow: 'auto',
  },
  chartContainer: {
    marginLeft: -22,
  },
  tableContainer: {
    height: 320,
  },
  h5: {
    marginBottom: theme.spacing.unit * 2,
  },
});

function postData(url, data = {}) {
  // Default options are marked with *
    return fetch(url, {
        method: "POST", // *GET, POST, PUT, DELETE, etc.
        mode: "cors", // no-cors, cors, *same-origin
        cache: "no-cache", // *default, no-cache, reload, force-cache, only-if-cached
        headers: {
            "Content-Type": "application/json; charset=utf-8",
            "Accept": "application/json; charset=utf-8",
            // "Content-Type": "application/x-www-form-urlencoded",
        },
        redirect: "follow", // manual, *follow, error
        referrer: "no-referrer", // no-referrer, *client
        body: JSON.stringify(data), // body data type must match "Content-Type" header
    });
}

class App extends Component {
  constructor(props) {
    super(props);

    this.state = {
      isShow: false,
      open: true,
      projects: []
    };

  }

  componentDidMount() {
    fetch(API_URL + '/projects').
       then(response => response.json())
      .then(data => { console.log('setting state'); console.log(data); this.setState({ projects: data }) });
  }

  showTime = () => {
    this.setState({ isShow: true })
  }

  handleDrawerOpen = () => {
    this.setState({ open: true });
  };

  handleDrawerClose = () => {
    this.setState({ open: false });
  };


  render() {
    const isShow = this.state.isShow;
    const { classes } = this.props;
    console.log('render state');
    if (!isShow) {
      return (
        <div className="App">

          <header className="App-header">
            <img src={logo} className="App-logo" alt="logo" />
            <p>
              Add <code>Project</code> and Scan to Rock.
              </p>

            <Button className="App-link" variant="contained" color="primary" onClick={this.showTime}>
              Rock Now
                </Button>
          </header>
        </div>
      );
    } else {
      return (<React.Fragment>
        <CssBaseline />
        <div className={classes.root}>
          <AppBar
            position="absolute"
            className={classNames(classes.appBar, this.state.open && classes.appBarShift)}
          >
            <Toolbar disableGutters={!this.state.open} className={classes.toolbar}>

              <IconButton
                color="inherit"
                aria-label="Open drawer"
                onClick={this.handleDrawerOpen}
                className={classNames(
                  classes.menuButton,
                  this.state.open && classes.menuButtonHidden,
                )}
              >
                <MenuIcon />
              </IconButton>
              <Typography
                component="h1"
                variant="h6"
                color="inherit"
                noWrap
                className={classes.title}
              >
                双兔
              </Typography>
              <div className={classes.search}>
                <div className={classes.searchIcon}>
                  <SearchIcon />
                </div>
                <InputBase
                  placeholder="Search…"
                  classes={{
                    root: classes.inputRoot,
                    input: classes.inputInput,
                  }}
                />
              </div>
              <IconButton color="inherit">
                <Badge badgeContent={4} color="secondary">
                  <NotificationsIcon />
                </Badge>
              </IconButton>
            </Toolbar>
            <div>

            </div>
          </AppBar>
          <Drawer
            variant="permanent"
            classes={{
              paper: classNames(classes.drawerPaper, !this.state.open && classes.drawerPaperClose),
            }}
            open={this.state.open}
          >
            <div className={classes.toolbarIcon}>
              <div>
                <Button aria-label="Add" >
                  <AddIcon />
                </Button>
                <Button aria-label="Edit" className={classes.button}>
                  <EditIcon />
                </Button>
                <Button disabled aria-label="Delete" className={classes.button}>
                  <DeleteIcon />
                </Button>
              </div>
              <IconButton onClick={this.handleDrawerClose}>
                <ChevronLeftIcon />
              </IconButton>

            </div>
            <Divider />

            <Divider />
            <div>
              {console.log(this.state.projects)}
              {this.state.projects.map((project, i) => <ProjectItem key={i}
                project={project} />)}

            </div>
          </Drawer>
          <main className={classes.content}>
            <div className={classes.appBarSpacer} />
            <Typography variant="h4" gutterBottom component="h2">
              Orders
            </Typography>
            <Typography component="div" className={classes.chartContainer}>
              {/* <SimpleLineChart /> */}
            </Typography>
            <Typography variant="h4" gutterBottom component="h2">
              Products
            </Typography>
            <div className={classes.tableContainer}>

            </div>
          </main>
        </div>
      </React.Fragment>);
    }
  }
}
App.propTypes = {
  classes: PropTypes.object.isRequired,
};

class ProjectItem extends Component {

  constructor(props) {
    super(props);

    this.state = {
      project : this.props.project
    };
    this.doScan = this.doScan.bind(this);
    this.cancelScan = this.cancelScan.bind(this);
    this.showResult = this.showResult.bind(this);
  }

  doScan() {
    console.log("in do scan");
    postData(API_URL + '/projects/scan/' + this.state.project.name)
    .then(response => {
      if (!response.ok) { throw response }
      return response.json()
    })
    .then(data => {
      this.setState({
        project: data
      });
    }) // JSON-string from `response.json()` call
    .catch(error => console.error(error));
  }

  cancelScan() {

  }

  showResult() {

  }

  render() {
    let actionIcon;
    let actionText;
    let actionHandler;

    actionHandler = this.doScan
    actionText = 'Scan';
    actionIcon = <ScanIcon />

    if (this.state.project.status === 'OnProgress') {
      actionText = 'Cancel'
      actionIcon = <CancelIcon />
      actionHandler = this.cancelScan
    } 

    return (
      <ListItem button>
        <ListItemIcon>
          <DashboardIcon />
        </ListItemIcon>
        <ListItemText primary={this.state.project.name} />
       
          <ListItemSecondaryAction>
            <IconButton aria-label={actionText} onClick={actionHandler}>
              {actionIcon}
            </IconButton>
          </ListItemSecondaryAction>
          <ListItemSecondaryAction>
            <IconButton aria-label="Result" onClick={this.showResult}>
            <ReportIcon/>
            </IconButton>
          </ListItemSecondaryAction>
      </ListItem>
    );
  }
}
export default withStyles(styles)(App);
