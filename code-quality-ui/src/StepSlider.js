import React from 'react';
import PropTypes from 'prop-types';
import { withStyles } from '@material-ui/core/styles';
import Slider from '@material-ui/lab/Slider';

const styles = {
  root: {
    width: '100%',
  },
  slider: {
    padding: '22px 0px',
  },
};

class StepSlider extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            value : props.value
        }
    }

  state = {
    value: 50,
  };

  handleChange = (event, value) => {
    this.setState({ value });
    if (this.props.onValueChanged) {
        this.props.onValueChanged(event, value);
    }
  };

  render() {
    const { classes } = this.props;
    const { value } = this.state;

    return (
      <div className={classes.root}>
        <Slider
          classes={{ container: classes.slider }}
          value={value}
          min={0}
          max={100}
          step={5}
          onChange={this.handleChange}
        />
      </div>
    );
  }
}

StepSlider.propTypes = {
  classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(StepSlider);