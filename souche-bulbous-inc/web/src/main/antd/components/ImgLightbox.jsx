import React from 'react';
import Lightbox from 'react-image-lightbox';

class ImgLightbox extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      photoIndex: 0,
      isOpen: false,
      images: props.images,
    };
  }

  componentWillReceiveProps = (nextProps) => {
    this.setState({
      images: nextProps.images
    })
  }


  render() {
    const {
      photoIndex,
      isOpen,
      images,
    } = this.state;

    return (
      <div>
        <a href="#" onClick={() => this.setState({ isOpen: true })}>查看图片</a>
        {isOpen &&
          <Lightbox
            mainSrc={images[photoIndex]}
            nextSrc={images[(photoIndex + 1) % images.length]}
            prevSrc={images[(photoIndex + images.length - 1) % images.length]}

            onCloseRequest={() => this.setState({ isOpen: false })}
            onMovePrevRequest={() => this.setState({
              photoIndex: (photoIndex + images.length - 1) % images.length,
            })}
            onMoveNextRequest={() => this.setState({
              photoIndex: (photoIndex + 1) % images.length,
            })}
          />
        }
      </div>
    );
  }
}
export default ImgLightbox;